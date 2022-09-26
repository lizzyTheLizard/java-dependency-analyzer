package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.model.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class JDepsRunnerTest {
    @Test
    public void run() {
        String fatJarFileName = ".\\src\\test\\resources\\fat-jar-example.jar";
        Element graph = JDepsRunner.create()
                .fatJar(fatJarFileName, f -> f.getName().startsWith("jackson-core"))
                .multiRelease("17")
                .run();
        String[] rootPackages = graph.getChildren()
                .map(Element::getFullName)
                .flatMap(Optional::stream)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(new String[]{"java", "org", "com", "ch", "reactor"}, rootPackages);

        String[] chPackages = graph.getChildren()
                .filter(e -> e.getName().orElse("").equals("ch"))
                .findFirst()
                .stream()
                .flatMap(Element::getChildren)
                .map(Element::getFullName)
                .flatMap(Optional::stream)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(new String[]{"ch.baselone"}, chPackages);

        String[] fasterxmlPackage = graph.getChildren()
                .filter(e -> e.getName().orElse("").equals("com"))
                .findFirst()
                .stream()
                .flatMap(Element::getChildren)
                .filter(e -> e.getName().orElse("").equals("fasterxml"))
                .findFirst()
                .stream()
                .flatMap(Element::getChildren)
                .map(Element::getFullName)
                .flatMap(Optional::stream)
                .toArray(String[]::new);
        Assertions.assertArrayEquals(new String[]{"com.fasterxml.jackson"}, fasterxmlPackage);

    }
}