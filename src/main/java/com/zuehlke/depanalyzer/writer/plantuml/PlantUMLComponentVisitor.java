package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Package;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor
class PlantUMLComponentVisitor implements Visitor {
    private final PrintStream printStream;

    @Override
    public void visitPackage(Package aPackage) {
        printStream.printf("package \"%s\" as %s { %n", aPackage.getName(), aPackage.hashCode());
    }

    @Override
    public void exitPackage(Package aPackage) {
        printStream.println("}");
    }

    @Override
    public void visitClass(Class aClass) {
        printStream.printf("class \"%s\" as %s %n", aClass.getName(), aClass.hashCode());
    }
}
