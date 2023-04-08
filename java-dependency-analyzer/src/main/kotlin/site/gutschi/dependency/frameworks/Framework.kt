package site.gutschi.dependency.frameworks

import org.objectweb.asm.ClassReader
import site.gutschi.dependency.write.Output.*

interface Framework {
    data class AttributeGenerator(
        val name: String,
        val type: AttributeType,
        val value: ((classReader: ClassReader) -> String)
    )

    fun getCollapsedPackages(nodes: Collection<Node>): List<String>

    fun getIgnoredPackages(nodes: Collection<Node>): List<String>

    fun getAttributes(classFile: ClassReader): Collection<Attribute>

}