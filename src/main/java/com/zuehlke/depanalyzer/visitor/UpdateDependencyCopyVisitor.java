package com.zuehlke.depanalyzer.visitor;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.DependencyGraphBuilder;
import com.zuehlke.depanalyzer.graph.Element;
import com.zuehlke.depanalyzer.graph.Package;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class UpdateDependencyCopyVisitor implements Visitor {
    private final DependencyGraphBuilder resultBuilder;
    private final BiConsumer<Element, Element> addDependency;



    @Override
    public void visitPackage(Package aPackage) {
        visitElement(aPackage);
    }

    @Override
    public void visitClass(Class aClass) {
        visitElement(aClass);
    }

    private void visitElement(Element element){
        Element newElement = resultBuilder.getOrAddElement(element.getFullName(), element.getClass());
        element.getDependencies().stream()
                .map(e -> resultBuilder.getOrAddElement(e.getFullName(), e.getClass()))
                .forEach(e -> addDependency.accept(newElement, e));
    }
}

