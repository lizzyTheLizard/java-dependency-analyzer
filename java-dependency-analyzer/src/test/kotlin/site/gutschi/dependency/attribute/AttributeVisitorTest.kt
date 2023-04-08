package site.gutschi.dependency.attribute

import site.gutschi.dependency.Properties
import site.gutschi.dependency.TestFileHelpers.Companion.getFile
import site.gutschi.dependency.asm.ClassFileFinder
import site.gutschi.dependency.assertNodesEquals
import kotlin.test.Test

internal class AttributeVisitorTest {
    @Test
    fun classFile() {
        val classFileFinder = ClassFileFinder(Properties(listOf(getFile("DependencyVisitorTest.class"))))
        val visitor = AttributeVisitor()
        classFileFinder.apply(visitor)
        assertNodesEquals("DependencyVisitorTest.deps.json", visitor.results)
    }
}
