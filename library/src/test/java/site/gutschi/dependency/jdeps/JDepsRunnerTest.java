package site.gutschi.dependency.jdeps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JDepsRunnerTest {
    //TODO: Write better test
    @Test
    void dummyTest() {
        String result = JDepsRunner.create()
                .file()
                .ignoreMissingDeps(true)
                .verbose(false)
                .multiRelease("9")
                .run();
        assertNotNull(result);
    }

    @Test
    void dummyTest2() throws URISyntaxException {
        URL url = getClass().getResource("/flatJarExample.jar");
        assert url != null;
        File file = new File(url.toURI());
        Assertions.assertThrows(JDepsException.class, () -> JDepsRunner.create()
                .fatJar(file.getAbsolutePath(), name -> true).file());

    }
}