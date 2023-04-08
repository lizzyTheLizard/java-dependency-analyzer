package site.gutschi.dependency

import site.gutschi.dependency.asm.ClassFileFinder
import site.gutschi.dependency.attribute.AttributeVisitor
import site.gutschi.dependency.dependency.DependencyVisitor
import site.gutschi.dependency.write.Node
import site.gutschi.dependency.write.Output
import site.gutschi.dependency.write.writeFile
import java.util.stream.Stream

class Main(private val properties: Properties) {
    fun execute() {
        val dependencyVisitor = DependencyVisitor(properties)
        val attributeVisitor = AttributeVisitor()
        val classFileFinder = ClassFileFinder(properties)
        classFileFinder.apply(dependencyVisitor, attributeVisitor)
        val nodes = collectNodes(dependencyVisitor, attributeVisitor)
        val dependencies = dependencyVisitor.results
        properties.outputFolder?.let {
            val output = Output(nodes, dependencies)
            writeFile(it, output)
            properties.log("Created documentation at '${it.absolutePath}'", Level.INFO)
        }
    }

    private fun collectNodes(
        dependencyVisitor: DependencyVisitor,
        attributeVisitor: AttributeVisitor
    ): Collection<Node> {
        val nodes = HashSet<Node>()
        nodes.addAll(attributeVisitor.results)
        dependencyVisitor.results.stream()
            .flatMap { Stream.of(it.from, it.to) }
            .filter { nodes.none { n -> n.fullName == it } }
            .forEach { nodes.add(Node(getSimpleName(it), it, listOf())) }
        return nodes
    }

    private fun getSimpleName(canonicalName: String): String {
        return canonicalName.substringAfterLast(".")
    }
}