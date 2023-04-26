package site.gutschi.dependency.frameworks

import org.objectweb.asm.ClassReader
import site.gutschi.dependency.AttributeCollector
import site.gutschi.dependency.AttributeCollector.AttributeGenerator
import site.gutschi.dependency.Output.Attribute
import site.gutschi.dependency.Output.Node


class Spring : AttributeCollector {
    private val collapsedPackages = listOf("org.springframework")
    private val ignoredPackages = listOf(
        "javax.validation",
        "jakarta.validation",
        "jakarta.persistence",
        "jakarta.annotation",
        "jakarta.servlet",
        "io.swagger"
    )
    private val generators = listOf<AttributeGenerator>()

    override fun getCollapsedPackages(nodes: Collection<Node>): List<String> {
        return collapsedPackages.filter { p -> nodes.any { n -> n.fullName.startsWith(p) } }
    }

    override fun getIgnoredPackages(nodes: Collection<Node>): List<String> {
        return ignoredPackages.filter { p -> nodes.any { n -> n.fullName.startsWith(p) } }
    }

    override fun getAttributes(classFile: ClassReader): Collection<Attribute> {
        return generators.map { Attribute(it.name, it.value(classFile), it.type) }
    }
}
