package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Package;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

@RequiredArgsConstructor
public class PlantUMLWriter implements Writer {
    private final File outFile;

    @Override
    public void write(DependencyGraph dependencyGraph){
        try(PrintStream printStream = new PrintStream(outFile)){
            printStream.println("@startuml");
            printStream.println("skinparam linetype ortho");
            PlantUMLComponentVisitor componentVisitor = new PlantUMLComponentVisitor(printStream);
            dependencyGraph.visit(componentVisitor);
            PlantUMLDependencyVisitor dependencyVisitor = new PlantUMLDependencyVisitor(printStream);
            dependencyGraph.visit(dependencyVisitor);
            printStream.println("@enduml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
