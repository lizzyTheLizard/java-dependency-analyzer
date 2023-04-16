package site.gutschi.dependency.asm

import org.objectweb.asm.ClassReader
import site.gutschi.dependency.Properties
import site.gutschi.dependency.Output.Node

class NodeVisitor(private val properties: Properties) {
    val results = HashSet<Node>()

    fun visit(classFile: ClassReader) {
        val attributes = properties.attributeCollectors.flatMap { it.getAttributes(classFile) }
        results.add(
            Node(
                name = AsmHelper.getSimpleName(classFile.className),
                fullName = AsmHelper.getCanonicalName(classFile.className),
                attributes = attributes
            )
        )
    }
}
