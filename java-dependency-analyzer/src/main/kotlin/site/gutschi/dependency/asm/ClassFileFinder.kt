package site.gutschi.dependency.asm

import org.codehaus.plexus.util.IOUtil
import org.objectweb.asm.ClassReader
import site.gutschi.dependency.Level
import site.gutschi.dependency.Properties
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarInputStream
import java.util.stream.Stream
import java.util.stream.StreamSupport


class ClassFileFinder(private val properties: Properties) {
    @Throws(VisitorException::class, ClassFileFinderException::class)
    fun apply(vararg visitors: Visitor) {
        properties.inputs.forEach { f ->
            try {
                apply(f, Level.INFO, visitors)
            } catch (e: VisitorException) {
                throw e
            } catch (e: ClassFileFinderException) {
                throw e
            } catch (e: Exception) {
                throw ClassFileFinderException.cannotReadFile(f, e)
            }
        }
    }

    private fun apply(file: File, logLevel: Level, visitors: Array<out Visitor>) {
        if (!file.exists()) {
            properties.log("File '${file.absolutePath}' does not exist", Level.WARN)
            return
        }
        if (file.isDirectory) {
            properties.log("Analyzing directory '${file.absolutePath}'", logLevel)
            file.listFiles()?.forEach { apply(it, Level.DEBUG, visitors) }
            return
        }
        if (file.name.endsWith(".class")) {
            properties.log("Analyzing class file '${file.absolutePath}'", logLevel)
            FileInputStream(file).use { analyzeClassFile(it, visitors) }
            return
        }
        if (file.name.endsWith(".jar")) {
            properties.log("Analyzing jar file '${file.absolutePath}'", logLevel)
            FileInputStream(file).use { analyzeJarFile(it, logLevel, visitors) }
            return
        }
        properties.log("File '${file.absolutePath}' cannot be analyzed", logLevel)
    }

    private fun analyzeClassFile(inputStream: InputStream, visitors: Array<out Visitor>) {
        val byteCode = IOUtil.toByteArray(inputStream)
        val reader = ClassReader(byteCode)
        visitors.forEach {
            it.visit(byteCode)
            it.visit(reader)
        }
    }

    private fun analyzeJarFile(inputStream: InputStream, logLevel: Level, visitors: Array<out Visitor>) {
        val zipStream = JarInputStream(inputStream)
        val springBootClasses = getSpringBootClassesAttribute(zipStream)
        if (springBootClasses != null && !properties.includeFatJarClasses) {
            properties.log("Import only BOOT-INF/classes as includeFatJarClasses is false", logLevel)
        }
        streamEntries(zipStream).forEach {
            if (isJarToInclude(it)) analyzeJarFile(zipStream, Level.DEBUG, visitors)
            if (isClassToInclude(it, springBootClasses)) analyzeClassFile(zipStream, visitors)
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
