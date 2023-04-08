package site.gutschi.dependency.attribute

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import site.gutschi.dependency.asm.Visitor
import site.gutschi.dependency.write.Attribute
import site.gutschi.dependency.write.AttributeType
import site.gutschi.dependency.write.Node

class AttributeVisitor : Visitor {
    val results = HashSet<Node>()

    override fun visit(classFile: ClassReader) {
        val attributes = HashSet<Attribute>()
        classFile.accept(getVisitor(attributes), 0)
        results.add(Node(getSimpleName(classFile.className), getCanonicalName(classFile.className), attributes))
    }

    private fun getCanonicalName(asmName: String): String {
        return asmName.replace(Regex("/"), ".")
    }

    private fun getSimpleName(asmName: String): String {
        return asmName.substringAfterLast("/")
    }

    private fun getVisitor(attributes: HashSet<Attribute>): ClassVisitor {
        return object : ClassVisitor(Opcodes.ASM9) {
            override fun visit(
                version: Int,
                access: Int,
                name: String?,
                signature: String?,
                superName: String?,
                interfaces: Array<out String>?
            ) {
                attributes.add(Attribute("Version", version.toString(), AttributeType.NUMBER))
                attributes.add(Attribute("Access", getAccess(access), AttributeType.TEXT))
                attributes.add(Attribute("Final", getFinal(access), AttributeType.BOOLEAN))
                attributes.add(Attribute("Type", getType(access), AttributeType.TEXT))
                attributes.add(Attribute("Deprecated", getDeprecated(access), AttributeType.BOOLEAN))
                superName?.let { attributes.add(Attribute("Super Class", getCanonicalName(it), AttributeType.TEXT)) }
                interfaces?.let { a ->
                    attributes.add(
                        Attribute(
                            "Interfaces",
                            a.joinToString(", ") { getCanonicalName(it) },
                            AttributeType.TEXT
                        )
                    )
                }
            }
        }
    }

    private fun getAccess(access: Int): String {
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

    private fun getFinal(access: Int): String {
        if (access and Opcodes.ACC_FINAL != 0) {
            return "True"
        }
        return "False"
    }

    private fun getDeprecated(access: Int): String {
        if (access and Opcodes.ACC_DEPRECATED != 0) {
            return "True"
        }
        return "False"
    }

    private fun getType(access: Int): String {
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
}
