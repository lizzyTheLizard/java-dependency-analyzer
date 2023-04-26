package site.gutschi.dependency

import site.gutschi.dependency.Output.Node
import site.gutschi.dependency.Output.OutputProperties
import site.gutschi.dependency.Properties.Level
import site.gutschi.dependency.asm.ClassFileCollection
import site.gutschi.dependency.write.FileWriter

/***
 * This is the main class of the dependency analyzer. It is called from the maven-plugin, but could also be called
 * from library or cli modules. You have to instantiate it using a property object and the call "execute" on
 * it to generate the documentation
 */
class Main(private val properties: Properties) {
    fun execute() {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        properties.outputFolder?.let {
            val outputProperties = OutputProperties(
                name = properties.name,
                version = properties.version,
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
        return properties.attributeCollectors.flatMap { it.getCollapsedPackages(nodes) }
    }

    private fun getIgnoredPackages(nodes: Collection<Node>): Collection<String> {
        if (properties.ignoredPackages.isNotEmpty()) {
            return properties.ignoredPackages
        }
        return properties.attributeCollectors.flatMap { it.getIgnoredPackages(nodes) }
    }

    private fun getSplitPackages(nodes: Collection<Node>): Collection<String> {
        if (properties.splitPackages.isNotEmpty()) {
            return properties.splitPackages
        }
        return properties.attributeCollectors.flatMap { it.getSplitPackages(nodes) }
    }
}