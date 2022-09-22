package com.zuehlke.depanalyzer;

import com.zuehlke.depanalyzer.filter.DependencyFilter;
import com.zuehlke.depanalyzer.filter.RemoveCommonRootFilter;
import com.zuehlke.depanalyzer.filter.RemoveElementsFilter;
import com.zuehlke.depanalyzer.jdeps.JDepsRunner;
import com.zuehlke.depanalyzer.writer.plantuml.PlantUMLWriter;

import java.io.File;

public class Main {
        public static void main(String[] args) {
            JDepsRunner.create()
                    .fatJar(new File("C:\\Users\\grma\\Downloads\\k-mis-ng-backend-6.0.52-SNAPSHOT.jar"), f -> f.getName().startsWith("Mis"))
                    .multiRelease("9")
                    .run()
                    .filter(RemoveElementsFilter.notStartsWith("ch.kessler.misng"))
                    .filter(RemoveCommonRootFilter.filter())
                    .filter(DependencyFilter.usePackageDependencies(true))
                    .write(new PlantUMLWriter(new File("./plantuml.txt")));
            }
}
