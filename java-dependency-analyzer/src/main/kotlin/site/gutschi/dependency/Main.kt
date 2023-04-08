package site.gutschi.dependency

import site.gutschi.dependency.Properties.Level
import site.gutschi.dependency.asm.ClassFileCollection
import site.gutschi.dependency.write.FileWriter
import site.gutschi.dependency.write.Output
import site.gutschi.dependency.write.Output.Node
import site.gutschi.dependency.write.Output.OutputProperties

class Main(private val properties: Properties) {
    fun execute() {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        properties.outputFolder?.let {
            val outputProperties = OutputProperties(
                basePackage = properties.basePackage,
                collapsePackages = getCollapsedPackages(classFileCollection.nodes),
                ignoredPackages = getIgnoredPackages(classFileCollection.nodes),
                splitPackages = getSplitPackages(classFileCollection.nodes)
            )
            val output = Output(classFileCollection.nodes, classFileCollection.dependencies, outputProperties)
            FileWriter(it).write(output)
            properties.log("Created documentation at '${it.absolutePath}'", Level.INFO)
        }
    }


    private fun getCollapsedPackages(nodes: Collection<Node>): Collection<String> {
        if (properties.collapsePackages.isNotEmpty()) {
            return properties.collapsePackages
        }
        return properties.frameworks.flatMap { it.getCollapsedPackages(nodes) }
    }

    private fun getIgnoredPackages(nodes: Collection<Node>): Collection<String> {
        if (properties.collapsePackages.isNotEmpty()) {
            return properties.collapsePackages
        }
        return properties.frameworks.flatMap { it.getIgnoredPackages(nodes) }
    }

    fun getSplitPackages(nodes: Collection<Node>): Collection<String>{
        if (properties.splitPackages.isNotEmpty()) {
            return properties.splitPackages
        }
        return properties.frameworks.flatMap { it.getSplitPackages(nodes) }
    }
}