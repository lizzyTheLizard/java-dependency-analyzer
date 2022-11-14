package site.gutschi.dependency.jdeps;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FatJarUnzipperTest {

    //TODO: Write better test
    @Test
    void dummyTest() throws IOException, URISyntaxException {
        URL url = getClass().getResource("/flatJarExample.jar");
        assert url != null;
        File file = new File(url.toURI());
        try (InputStream io = new FileInputStream(file)) {
            Path path = FatJarUnzipper.unzip(io);
            assertNotNull(path);
        }
    }
}