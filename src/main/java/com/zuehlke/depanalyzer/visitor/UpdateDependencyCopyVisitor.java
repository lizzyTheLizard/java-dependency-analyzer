package com.zuehlke.depanalyzer.visitor;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.DependencyGraphBuilder;
import com.zuehlke.depanalyzer.graph.Element;
import com.zuehlke.depanalyzer.graph.Package;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class UpdateDependencyCopyVisitor implements Visitor {
    private final DependencyGraphBuilder resultBuilder;
    private final BiConsumer<Element, Element> addDependency;

    @Override
    public void visitPackage(Package aPackage) {
        Package newPackage = resultBuilder.getOrAddPackage(aPackage.getFullName());
        addDependencies(newPackage, aPackage.getDependencies());
    }

    @Override
    public void visitClass(com.zuehlke.depanalyzer.graph.Class aClass) {
        Class newClass = resultBuilder.getOrAddClass(aClass.getFullName());
        addDependencies(newClass, aClass.getDependencies());
    }

    private void addDependencies(Element from, Set<Element> to) {
        to.stream()
                .map(e -> resultBuilder.getOrAddElement(e.getFullName(), e.getClass()))
                .forEach(e -> addDependency.accept(from, e));
    }
}

