package site.gutschi.dependency

import site.gutschi.dependency.Properties.Level
import site.gutschi.dependency.asm.ClassFileCollection
import site.gutschi.dependency.frameworks.JavaBase
import site.gutschi.dependency.write.FileWriter
import site.gutschi.dependency.write.Output
import site.gutschi.dependency.write.Output.Node
import site.gutschi.dependency.write.Output.OutputProperties

class Main(private val properties: Properties) {
    private val javaBase = JavaBase()


    fun execute() {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        properties.outputFolder?.let {
            val outputProperties = OutputProperties(
                basePackage = properties.basePackage,
                collapsePackages = getCollapsedPackages(classFileCollection.nodes),
                ignoredPackages = getIgnoredPackages(classFileCollection.nodes)
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
        return javaBase.getCollapsedPackages(nodes)
    }

    private fun getIgnoredPackages(nodes: Collection<Node>): Collection<String> {
        if (properties.collapsePackages.isNotEmpty()) {
            return properties.collapsePackages
        }
        return javaBase.getIgnoredPackages(nodes)
    }

}