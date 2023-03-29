package site.gutschi.dependency.write

import site.gutschi.dependency.jdeps.JDepsResultLine
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


internal class WriteFileTest {
    @Test
    fun createSimpleFile() {
        val input = listOf(JDepsResultLine("from", "to", null))
        val tempDir = createTempDirectory()
        writeFile(outputDir = tempDir.toFile(), jdepsOutput = input)
        val indexFile = tempDir.resolve("index.html")
        assertTrue(Files.exists(indexFile))
        assertTrue(Files.readString(indexFile).contains("from -> to"))
    }

    @Test
    fun cannotWrite() {
        val input = listOf(JDepsResultLine("from", "to", null))
        val noExisting = Path.of("/WRONG/IMPOSSIBLE/ANOTHER")
        assertFailsWith<WriteFileException>(
            block = {
                writeFile(outputDir = noExisting.toFile(), jdepsOutput = input)
            }
        )
    }
}
