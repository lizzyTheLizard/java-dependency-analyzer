package site.gutschi.dependency.asm

import site.gutschi.dependency.Properties
import site.gutschi.dependency.TestFileHelpers.Companion.getFile
import site.gutschi.dependency.assertNodesEquals
import site.gutschi.dependency.write.Output
import kotlin.test.Test

internal class NodeVisitorTest {
    @Test
    fun classFile() {
        val properties = Properties(listOf(getFile("DependencyVisitorTest.class")))
        val results = analyze(properties)
        assertNodesEquals("DependencyVisitorTest.deps.json", results)
    }


    private fun analyze(properties: Properties): Collection<Output.Node> {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        return classFileCollection.nodes
    }

}
