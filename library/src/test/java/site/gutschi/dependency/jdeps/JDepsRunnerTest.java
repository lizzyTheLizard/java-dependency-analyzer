package site.gutschi.dependency.jdeps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class JDepsRunnerTest {
    @Test
    void fileTest() throws URISyntaxException {
        URL url = getClass().getResource("/flatJarExample.jar");
        assert url != null;
        String result = JDepsRunner.create()
                .file(new File(url.toURI()))
                .run();
        System.out.println(result);
        assertTrue(result.lines().anyMatch(l -> l.startsWith("   site.gutschi.dependency.jdeps.FatJarUnzipper       -> java.util.zip.ZipEntry")));
    }

    @Test
    //TODO: Write better test
    void fatJarTest() throws URISyntaxException {
        URL url = getClass().getResource("/flatJarExample.jar");
        assert url != null;
        File file = new File(url.toURI());
        Assertions.assertThrows(JDepsException.class, () -> JDepsRunner.create()
                .fatJar(file.getAbsolutePath(), name -> true).file());

    }
}