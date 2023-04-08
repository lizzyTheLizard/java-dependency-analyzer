package site.gutschi.dependency.maven

import site.gutschi.dependency.TestFileHelpers
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertTrue


internal class DocumentationMojoTest {
    @Test
    fun dummy() {
        val target = DocumentationMojo()
        val tempDir = createTempDirectory()
        target.inputs = listOf(TestFileHelpers.getFile("flat-demo-0.0.1-SNAPSHOT.jar").absolutePath)
        target.outputFolder = tempDir.absolutePathString()
        target.execute()
        assertTrue(tempDir.resolve("index.html").exists())
    }
}
