package site.gutschi.dependency.write

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val output = Output(
    nodes = listOf(
        Output.Node("from", "from", listOf()),
        Output.Node("to", "to", listOf())
    ),
    dependencies = listOf(Output.Dependency("from", "to")),
    properties = Output.OutputProperties(
        name ="",
        version ="",
        basePackage = null,
        collapsePackages = listOf(),
        ignoredPackages = listOf()
    )
)

internal class WriteFileTest {
    @Test
    fun createSimpleFile() {
        val tmpDir = createTempDirectory()
        FileWriter(tmpDir.toFile()).write(output)
        val indexFile = tmpDir.resolve("index.html")
        assertTrue(Files.exists(indexFile))
    }

    @Test
    fun cannotWrite() {
        val noExisting = Path.of("/WRONG/IMPOSSIBLE/ANOTHER")
        assertFailsWith<WriteFileException>(
            block = {
                FileWriter(noExisting.toFile()).write(output)
            }
        )
    }
}
