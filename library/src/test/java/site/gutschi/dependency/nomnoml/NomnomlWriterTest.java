package site.gutschi.dependency.nomnoml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NomnomlWriterTest {
    @Test
    void createSimpleFile(@TempDir Path tempDir) throws IOException {
        NomnomlWriter.create()
                .outputDir(tempDir.toFile())
                .jdepsOutput("THIS IS A TEST")
                .write();

        Path indexFile = tempDir.resolve("index.html");
        Assertions.assertTrue(Files.exists(indexFile), "File has been created");
        Assertions.assertTrue(Files.readString(indexFile).contains("THIS IS A TEST"), "Input has been merged");
    }

    @Test
    void cannotWrite() {
        Path noExisting = Path.of("/WRONG/IMPOSSIBLE/ANOTHER");
        Assertions.assertThrows(NomnumlException.class, () -> NomnomlWriter.create()
                .outputDir(noExisting.toFile())
                .jdepsOutput("THIS IS A TEST")
                .write());
    }

    @Test
    void missingInput(@TempDir Path tempDir) {
        Assertions.assertThrows(NomnumlException.class, () -> NomnomlWriter.create()
                .outputDir(tempDir.toFile())
                .write());
    }

    @Test
    void missingOutputDir() {
        Assertions.assertThrows(NomnumlException.class, () -> NomnomlWriter.create()
                .jdepsOutput("THIS IS A TEST")
                .write());
    }
}