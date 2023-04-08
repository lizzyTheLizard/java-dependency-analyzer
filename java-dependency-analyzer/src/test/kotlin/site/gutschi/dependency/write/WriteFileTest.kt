package site.gutschi.dependency.write

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val output = Output(
    listOf(
        Node("from", "from", listOf()),
        Node("to", "to", listOf())
    ), listOf(
        Dependency("from", "to")
    )
)

internal class WriteFileTest {
    @Test
    fun createSimpleFile() {
        val tempDir = createTempDirectory()
        writeFile(tempDir.toFile(), output)
        val indexFile = tempDir.resolve("index.html")
        assertTrue(Files.exists(indexFile))
        assertTrue(
            Files.readString(indexFile)
                .contains("{\"nodes\":[{\"name\":\"from\",\"fullName\":\"from\",\"attributes\":[]},{\"name\":\"to\",\"fullName\":\"to\",\"attributes\":[]}],\"dependencies\":[{\"from\":\"from\",\"to\":\"to\"}]}")
        )
    }

    @Test
    fun cannotWrite() {
        val noExisting = Path.of("/WRONG/IMPOSSIBLE/ANOTHER")
        assertFailsWith<WriteFileException>(
            block = {
                writeFile(noExisting.toFile(), output)
            }
        )
    }
}
