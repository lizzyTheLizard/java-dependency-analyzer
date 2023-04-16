package site.gutschi.dependency.asm

import site.gutschi.dependency.Output
import site.gutschi.dependency.Properties
import site.gutschi.dependency.TestFileHelpers.Companion.getFile
import site.gutschi.dependency.assertNodesEquals
import site.gutschi.dependency.frameworks.JavaBase
import site.gutschi.dependency.frameworks.Spring
import kotlin.test.Test

internal class NodeVisitorTest {
    @Test
    fun classFile() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("DependencyVisitor.class")),
            attributeCollectors = listOf(JavaBase(), Spring())
        )
        val results = analyze(properties)
        assertNodesEquals("DependencyVisitor.deps.json", results)
    }


    private fun analyze(properties: Properties): Collection<Output.Node> {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        return classFileCollection.nodes
    }

}
