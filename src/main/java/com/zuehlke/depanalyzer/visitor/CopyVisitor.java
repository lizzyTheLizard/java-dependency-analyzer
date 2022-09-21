package com.zuehlke.depanalyzer.visitor;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Package;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class CopyVisitor implements Visitor {
    private final DependencyGraphBuilder resultBuilder;
    private final Function<Element, Boolean> filter;
    private final Function<Element, String> newName;

    @Override
    public void visitPackage(Package aPackage) {
        if(filter.apply(aPackage)) {
            return;
        }
        Package newPackage = resultBuilder.getOrAddPackage(newName.apply(aPackage));
        aPackage.getDependencies().stream()
                .filter(d -> !filter.apply(d))
                .map(d -> resultBuilder.getOrAddElement(newName.apply(d), d.getClass()))
                .forEach(e -> resultBuilder.addDependency(newPackage, e));
    }

    @Override
    public void visitClass(Class aClass) {
        if(filter.apply(aClass)) {
            return;
        }
        Class newClass = resultBuilder.getOrAddClass(newName.apply(aClass));
        aClass.getDependencies().stream()
                .filter(d -> !filter.apply(d))
                .map(d -> resultBuilder.getOrAddElement(newName.apply(d), d.getClass()))
                .forEach(e -> resultBuilder.addDependency(newClass, e));
    }
 }
