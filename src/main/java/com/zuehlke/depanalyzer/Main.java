package com.zuehlke.depanalyzer;

import com.zuehlke.depanalyzer.filter.DependencyFilter;
import com.zuehlke.depanalyzer.filter.RemoveCommonRootFilter;
import com.zuehlke.depanalyzer.filter.RemoveElementsFilter;
import com.zuehlke.depanalyzer.jdeps.JDepsRunner;
import com.zuehlke.depanalyzer.writer.plantuml.PlantUMLWriter;

import java.io.File;

public class Main {
        public static void main(String[] args) {
                new JDepsRunner()
                        .run(new File("./target/"))
                        .filter(RemoveElementsFilter.startsWith("java"))
                        .filter(RemoveCommonRootFilter.filter())
                        .filter(DependencyFilter.usePackageDependencies())
                        .filter(RemoveElementsFilter.allClasses())
                        .write(new PlantUMLWriter(new File("./plantuml.txt")));
            }
}
