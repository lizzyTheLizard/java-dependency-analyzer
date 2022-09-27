package com.zuehlke.depanalyzer.jdeps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

public class FatJarUnzipperTest {
    @Test
    public void unzip() throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("fat-jar-example.jar")) {
            Path output = FatJarUnzipper.unzip(is);

            File libFolder = output.resolve("BOOT-INF").resolve("lib").toFile();
            Assertions.assertTrue(libFolder.exists());
            String[] libs = Optional.of(libFolder)
                    .map(File::listFiles)
                    .stream()
                    .flatMap(Arrays::stream)
                    .map(File::getName)
                    .toArray(String[]::new);
            System.out.println(String.join("\",\"", libs));
            Assertions.assertEquals(46, libs.length);
            Assertions.assertTrue(Arrays.stream(libs).anyMatch("jackson-annotations-2.13.4.jar":: equals));

            File classesFolder = output.resolve("BOOT-INF").resolve("classes").toFile();
            Assertions.assertTrue(classesFolder.exists());
            String[] classes = Optional.of(classesFolder)
                    .map(File::listFiles)
                    .stream()
                    .flatMap(Arrays::stream)
                    .map(File::getName)
                    .toArray(String[]::new);
            Assertions.assertArrayEquals(new String[]{"ch"}, classes);
        }
    }
}
