package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Package;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor
class PlantUMLDependencyVisitor implements Visitor {
    private final PrintStream printStream;
    @Override
    public void visitClass(Class aClass) {
        aClass.getDependencies().forEach(d -> printStream.printf("%s --> %s%n", aClass.getName(), d.getName()));
    }

    @Override
    public void visitPackage(Package aPackage) {
        aPackage.getDependencies().forEach(d -> printStream.printf("%s --> %s%n", aPackage.getName(), d.getName()));
    }

}
