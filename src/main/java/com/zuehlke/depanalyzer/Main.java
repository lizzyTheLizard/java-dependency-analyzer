package com.zuehlke.depanalyzer;

import com.zuehlke.depanalyzer.jdeps.JDepsRunner;
import com.zuehlke.depanalyzer.mapper.FilterPackageMapper;
import com.zuehlke.depanalyzer.mapper.PackageDependencyMapper;
import com.zuehlke.depanalyzer.mapper.SelectPackageMapper;
import com.zuehlke.depanalyzer.mapper.SimplifyPackageMapper;
import com.zuehlke.depanalyzer.writer.plantuml.PlantUMLWriter;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String out = JDepsRunner.create()
                .fatJar("./src/test/resources/fat-jar-example.jar", f -> f.getName().startsWith("jackson-annotations"))
                //.fatJar("C:\\Users\\grma\\Downloads\\k-mis-ng-backend-6.0.52-SNAPSHOT.jar", f -> f.getName().startsWith("Mis"))
                //.file("./target")
                .multiRelease("9")
                .run()
                .apply(new SelectPackageMapper("ch.kessler"))
                .apply(FilterPackageMapper.doesNotContain(".ko3"))
                .apply(new SimplifyPackageMapper("ch.kessler.misng.core.biz.reports"))
                .apply(new PackageDependencyMapper(true))
                .apply(new PlantUMLWriter());

        try (FileWriter writer = new FileWriter("./plantuml.txt")) {
            writer.write(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
