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
            Assertions.assertArrayEquals(new String[]{"jackson-annotations-2.13.4.jar", "jackson-core-2.13.4.jar", "jackson-databind-2.13.4.jar", "jackson-datatype-jdk8-2.13.4.jar", "jackson-datatype-jsr310-2.13.4.jar", "jackson-module-parameter-names-2.13.4.jar", "jakarta.annotation-api-1.3.5.jar", "jul-to-slf4j-1.7.36.jar", "log4j-api-2.17.2.jar", "log4j-to-slf4j-2.17.2.jar", "logback-classic-1.2.11.jar", "logback-core-1.2.11.jar", "netty-buffer-4.1.82.Final.jar", "netty-codec-4.1.82.Final.jar", "netty-codec-dns-4.1.82.Final.jar", "netty-codec-http-4.1.82.Final.jar", "netty-codec-http2-4.1.82.Final.jar", "netty-codec-socks-4.1.82.Final.jar", "netty-common-4.1.82.Final.jar", "netty-handler-4.1.82.Final.jar", "netty-handler-proxy-4.1.82.Final.jar", "netty-resolver-4.1.82.Final.jar", "netty-resolver-dns-4.1.82.Final.jar", "netty-resolver-dns-classes-macos-4.1.82.Final.jar", "netty-resolver-dns-native-macos-4.1.82.Final-osx-x86_64.jar", "netty-transport-4.1.82.Final.jar", "netty-transport-classes-epoll-4.1.82.Final.jar", "netty-transport-native-epoll-4.1.82.Final-linux-x86_64.jar", "netty-transport-native-unix-common-4.1.82.Final.jar", "reactive-streams-1.0.4.jar", "reactor-core-3.4.23.jar", "reactor-netty-core-1.0.23.jar", "reactor-netty-http-1.0.23.jar", "slf4j-api-1.7.36.jar", "snakeyaml-1.30.jar", "spring-aop-5.3.23.jar", "spring-beans-5.3.23.jar", "spring-boot-2.7.4.jar", "spring-boot-autoconfigure-2.7.4.jar", "spring-boot-jarmode-layertools-2.7.4.jar", "spring-context-5.3.23.jar", "spring-core-5.3.23.jar", "spring-expression-5.3.23.jar", "spring-jcl-5.3.23.jar", "spring-web-5.3.23.jar", "spring-webflux-5.3.23.jar"}, libs);

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
