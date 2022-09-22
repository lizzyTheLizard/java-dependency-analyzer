package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Element;
import com.zuehlke.depanalyzer.graph.Package;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;

@RequiredArgsConstructor
class PlantUMLDependencyVisitor implements Visitor {
    private final PrintStream printStream;
    @Override
    public void visitClass(Class aClass) {
        visitElement(aClass);
    }

    @Override
    public void visitPackage(Package aPackage) {
        visitElement(aPackage);
    }

    private void visitElement(Element element){
        element.getDependencies().forEach(d -> printStream.printf("%s --> %s%n", element.hashCode(), d.hashCode()));

    }

}
