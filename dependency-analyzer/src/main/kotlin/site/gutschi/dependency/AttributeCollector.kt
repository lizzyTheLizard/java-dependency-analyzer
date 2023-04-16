package site.gutschi.dependency

import org.objectweb.asm.ClassReader
import site.gutschi.dependency.Output.*

interface AttributeCollector {
    data class AttributeGenerator(
        val name: String,
        val type: AttributeType,
        val value: ((classReader: ClassReader) -> String)
    )

    fun getAttributes(classFile: ClassReader): Collection<Attribute> {
        return listOf()
    }

    fun getCollapsedPackages(nodes: Collection<Node>): Collection<String>{
        return listOf()
    }

    fun getIgnoredPackages(nodes: Collection<Node>): Collection<String>{
        return listOf()
    }

    fun getSplitPackages(nodes: Collection<Node>): Collection<String> {
        return listOf()
    }
}