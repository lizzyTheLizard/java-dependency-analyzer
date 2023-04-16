package site.gutschi.dependency.asm

import org.apache.maven.shared.dependency.analyzer.asm.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import site.gutschi.dependency.Properties
import site.gutschi.dependency.Properties.Level
import site.gutschi.dependency.Output.Dependency

class DependencyVisitor(private val properties: Properties) {
    val results = HashSet<Dependency>()

    fun visit(classFile: ByteArray) {
        val reader = ClassReader(classFile)
        val className = getClassName(reader)
        val collector = ResultCollector()
        collectStatic(classFile, collector)
        reader.accept(createVisitor(collector), 0)
        properties.log("Collected ${collector.dependencies.size} dependencies for class '$className'", Level.DEBUG)
        collector.dependencies.filter { it != className }.forEach { results.add(Dependency(className, it)) }
    }

    private fun getClassName(reader: ClassReader): String {
        return reader.className.replace(Regex("/"), ".")
    }

    private fun createVisitor(collector: ResultCollector): ClassVisitor {
        val annotationVisitor = DefaultAnnotationVisitor(collector)
        val signatureVisitor = DefaultSignatureVisitor(collector)
        val fieldVisitor = DefaultFieldVisitor(annotationVisitor, collector)
        val methodVisitor = DefaultMethodVisitor(annotationVisitor, signatureVisitor, collector)
        return DefaultClassVisitor(signatureVisitor, annotationVisitor, fieldVisitor, methodVisitor, collector)
    }

    private fun collectStatic(byteCode: ByteArray, collector: ResultCollector) {
        val method =
            ConstantPoolParser::class.java.getDeclaredMethod("getConstantPoolClassReferences", ByteArray::class.java)
        method.isAccessible = true
        val result = method.invoke(null, byteCode) as Set<*>
        result.forEach { s -> collector.addName(s as String) }
    }
}
