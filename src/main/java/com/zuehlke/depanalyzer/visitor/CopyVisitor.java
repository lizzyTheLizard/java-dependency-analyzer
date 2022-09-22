package com.zuehlke.depanalyzer.visitor;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Package;
import lombok.RequiredArgsConstructor;

import java.util.Stack;
import java.util.function.Function;

@RequiredArgsConstructor
public class CopyVisitor implements Visitor {
    private final DependencyGraphBuilder resultBuilder;
    private final Function<Element, Boolean> filter;
    private final Function<Element, Stack<String>> newName;

    @Override
    public void visitPackage(Package aPackage) {
        visitElement(aPackage);
    }

    @Override
    public void visitClass(Class aClass) {
        visitElement(aClass);
    }

    private void visitElement(Element element){
        if(filter.apply(element)) {
            return;
        }
        Element newClass = resultBuilder.getOrAddElement(newName.apply(element), element.getClass());
        element.getDependencies().stream()
                .filter(d -> !filter.apply(d))
                .map(d -> resultBuilder.getOrAddElement(newName.apply(d), d.getClass()))
                .forEach(e -> resultBuilder.addDependency(newClass, e));

    }
 }
