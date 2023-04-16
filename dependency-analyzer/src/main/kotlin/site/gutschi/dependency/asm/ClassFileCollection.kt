package site.gutschi.dependency.asm

import org.codehaus.plexus.util.IOUtil
import org.objectweb.asm.ClassReader
import site.gutschi.dependency.Output.Dependency
import site.gutschi.dependency.Output.Node
import site.gutschi.dependency.Properties
import site.gutschi.dependency.Properties.Level
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import java.util.stream.Stream
import java.util.stream.StreamSupport

class ClassFileCollection(private val properties: Properties) {
    private val dependencyVisitor = DependencyVisitor(properties)
    private val nodeVisitor = NodeVisitor(properties)

    val nodes: Collection<Node>
        get() {
            val nodes = HashSet<Node>()
            nodes.addAll(nodeVisitor.results)
            dependencyVisitor.results.stream()
                .flatMap { Stream.of(it.from, it.to) }
                .filter { nodes.none { n -> n.fullName == it } }
                .forEach {
                    nodes.add(
                        Node(
                            name = it.substringAfterLast("."),
                            fullName = it,
                            attributes = listOf()
                        )
                    )
                }
            return nodes
        }

    val dependencies: Collection<Dependency>
        get() {
            return dependencyVisitor.results
        }

    @Throws(AsmException::class)
    fun apply() {
        properties.inputs.forEach { f ->
            try {
                apply(f, Level.INFO)
            } catch (e: AsmException) {
                throw e
            } catch (e: Exception) {
                throw AsmException.cannotReadFile(f, e)
            }
        }
    }

    private fun apply(file: File, logLevel: Level) {
        if (!file.exists()) {
            properties.log("File '${file.absolutePath}' does not exist", Level.WARN)
            return
        }
        if (file.isDirectory) {
            properties.log("Analyzing directory '${file.absolutePath}'", logLevel)
            file.listFiles()?.forEach { apply(it, Level.DEBUG) }
            return
        }
        if (file.name.endsWith(".class")) {
            properties.log("Analyzing class file '${file.absolutePath}'", logLevel)
            FileInputStream(file).use { analyzeClassFile(it) }
            return
        }
        if (file.name.endsWith(".jar")) {
            properties.log("Analyzing jar file '${file.absolutePath}'", logLevel)
            FileInputStream(file).use { analyzeJarFile(it, logLevel) }
            return
        }
        properties.log("File '${file.absolutePath}' cannot be analyzed", logLevel)
    }

    private fun analyzeClassFile(inputStream: InputStream) {
        val byteCode = IOUtil.toByteArray(inputStream)
        dependencyVisitor.visit(byteCode)
        val reader = ClassReader(byteCode)
        nodeVisitor.visit(reader)
    }

    private fun analyzeJarFile(inputStream: InputStream, logLevel: Level) {
        val zipStream = JarInputStream(inputStream)
        val springBootClasses = getSpringBootClassesAttribute(zipStream)
        if (springBootClasses != null && !properties.includeFatJarClasses) {
            properties.log("Import only BOOT-INF/classes as includeFatJarClasses is false", logLevel)
        }
        streamEntries(zipStream).forEach {
            if (isJarToInclude(it)) analyzeJarFile(zipStream, Level.DEBUG)
            if (isClassToInclude(it, springBootClasses)) analyzeClassFile(zipStream)
        }
    }

    private fun isJarToInclude(jarEntry: JarEntry): Boolean {
        val filename = jarEntry.name.substringAfterLast("/")
        if (!filename.endsWith(".jar")) {
            return false
        }
        if (properties.fatJarMatchers.none { it.matches(filename) }) {
            properties.log("Ignoring dependency jar file '$filename'", Level.DEBUG)
            return false
        }
        properties.log("Analyzing dependency jar file '$filename'", Level.INFO)
        return true
    }

    private fun isClassToInclude(jarEntry: JarEntry, springBootClasses: String?): Boolean {
        val name = jarEntry.name
        if (!name.endsWith(".class")) {
            return false
        }
        if (springBootClasses != null && name.startsWith(springBootClasses)) {
            //This is a fat-jar , so include this folder
            properties.log("Include '$name' as its in the spring-boot-classes dir", Level.DEBUG)
            return true
        }
        if (springBootClasses == null) {
            properties.log("Include '$name' as this is not a fat jar", Level.DEBUG)
            return true
        }
        if (properties.includeFatJarClasses) {
            properties.log("Include '$name' as includeFatJarClasses is set to true", Level.DEBUG)
            return true
        }
        return false
    }

    private fun streamEntries(jarInputStream: JarInputStream): Stream<JarEntry> {
        val iterator = object : Iterator<JarEntry> {
            private var next: JarEntry? = null
            override fun hasNext(): Boolean {
                if (next != null) {
                    return true
                }
                next = jarInputStream.nextJarEntry
                return next != null
            }

            override fun next(): JarEntry {
                val result = next
                next = null
                if (result != null) {
                    return result
                }
                return jarInputStream.nextJarEntry
            }
        }
        val spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.IMMUTABLE)
        return StreamSupport.stream(spliterator, false)
    }

    private fun getSpringBootClassesAttribute(zipStream: JarInputStream): String? {
        val attributes = zipStream.manifest.mainAttributes
        val springBootClassesName = java.util.jar.Attributes.Name("Spring-Boot-Classes")
        return attributes[springBootClassesName]?.toString()
    }
}
