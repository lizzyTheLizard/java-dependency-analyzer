package site.gutschi.dependency.frameworks

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import site.gutschi.dependency.asm.AsmHelper
import site.gutschi.dependency.frameworks.Framework.AttributeGenerator
import site.gutschi.dependency.write.Output.*


class JavaBase : Framework {
    private val ignoredPackages = listOf("java.lang", "kotlin")
    private val collapsedPackages = listOf("javax.validation")
    private val generators = listOf(
        AttributeGenerator("Access", AttributeType.TEXT) { getAccess(it) },
        AttributeGenerator("Final", AttributeType.BOOLEAN) { getFinal(it) },
        AttributeGenerator("Type", AttributeType.TEXT) { getType(it) },
        AttributeGenerator("Implemented Interfaces", AttributeType.TEXT) { getInterfaces(it) },
        AttributeGenerator("Base Class", AttributeType.TEXT) { getBaseClass(it) }
    )

    override fun getCollapsedPackages(nodes: Collection<Node>): List<String> {
        return collapsedPackages.filter { p -> nodes.any { n -> n.fullName.startsWith(p) } }
    }

    override fun getIgnoredPackages(nodes: Collection<Node>): List<String> {
        return ignoredPackages.filter { p -> nodes.any { n -> n.fullName.startsWith(p) } }
    }

    override fun getAttributes(classFile: ClassReader): Collection<Attribute> {
        return generators.map { Attribute(it.name, it.value(classFile), it.type) }
    }

    private fun getAccess(classFile: ClassReader): String {
        val access = classFile.access
        if (access and Opcodes.ACC_PUBLIC != 0) {
            return "Public"
        }
        if (access and Opcodes.ACC_PRIVATE != 0) {
            return "Private"
        }
        if (access and Opcodes.ACC_PROTECTED != 0) {
            return "Protected"
        }
        return "Package"
    }

    private fun getFinal(classFile: ClassReader): String {
        val access = classFile.access
        if (access and Opcodes.ACC_FINAL != 0) {
            return "True"
        }
        return "False"
    }

    private fun getType(classFile: ClassReader): String {
        val access = classFile.access
        if (access and Opcodes.ACC_ANNOTATION != 0) {
            return "Annotation"
        }
        if (access and Opcodes.ACC_INTERFACE != 0) {
            return "Interface"
        }
        if (access and Opcodes.ACC_RECORD != 0) {
            return "Record"
        }
        if (access and Opcodes.ACC_ENUM != 0) {
            return "Enum"
        }
        if (access and Opcodes.ACC_ABSTRACT != 0) {
            return "Abstract"
        }
        return "Class"
    }

    private fun getInterfaces(classFile: ClassReader): String {
        return classFile.interfaces.joinToString(",") { AsmHelper.getCanonicalName(it) }
    }

    private fun getBaseClass(classFile: ClassReader): String {
        return AsmHelper.getCanonicalName(classFile.superName)
    }
}
