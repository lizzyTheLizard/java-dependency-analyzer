package com.zuehlke.depanalyzer.graph;


import java.util.*;

public class DependencyGraphBuilder {
    private final DependencyGraph result = new DependencyGraph();

    public DependencyGraph build(){
        return result;
    }

    public void addDependency(Element from, Element to){
        from.dependencies.add(to);
    }

    public Element getOrAddElement(Stack<String> fullName, java.lang.Class<? extends Element> aClass) {
        return getOrAddElementRecursive(result, fullName, aClass);
    }

    private Element getOrAddElementRecursive(Element parent, Stack<String> fullName, java.lang.Class<? extends Element> aClass) {
        if(fullName.size() == 1) {
            return getOrAddElement(parent, fullName.get(0), aClass);
        }
        Element nextPackage = getOrAddElement(parent, fullName.get(0), Package.class);
        fullName.remove(0);
        return getOrAddElementRecursive(nextPackage, fullName, aClass);
    }

    private Element getOrAddElement(Element parent, String name, java.lang.Class<? extends Element> aClass) {
        return parent.children.stream()
                .filter(x -> x.getName().equals(name))
                .filter(aClass::isInstance)
                .findFirst()
                .orElseGet(() -> {
                    Element result = createNewElement(parent, name, aClass);
                    parent.children.add(result);
                    return result;
                });
    }

    private Element createNewElement(Element parent, String name, java.lang.Class<? extends Element> aClass){
        if(aClass == Class.class) {
            return new Class(name, parent);
        }
        if(aClass == Package.class) {
            return new Package(name, parent);
        }
        throw new RuntimeException("Unknown Subclass " + aClass);
    }
}
