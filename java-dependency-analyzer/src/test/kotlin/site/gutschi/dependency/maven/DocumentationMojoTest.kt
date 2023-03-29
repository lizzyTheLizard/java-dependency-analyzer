package site.gutschi.dependency.maven

import site.gutschi.dependency.TestFileHelpers
import site.gutschi.dependency.jdeps.JDepsResultLine
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.exists
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


internal class DocumentationMojoTest {
    @Test
    fun dummy() {
        val target = DocumentationMojo()
        val tempDir = createTempDirectory()
        target.input = TestFileHelpers.FLAT_JAR.absolutePath
        target.outputFolder = tempDir.absolutePathString()
        target.execute()
        assertTrue(tempDir.resolve("index.html").exists())
    }
}
