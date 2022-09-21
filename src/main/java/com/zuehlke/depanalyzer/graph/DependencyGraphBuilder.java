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

    public Element getOrAddElement(String fullName, java.lang.Class<? extends Element> aClass) {
        if(aClass == Package.class) {
            return getOrAddPackage(fullName);
        }
        if(aClass == Class.class) {
            return getOrAddClass(fullName);
        }
        throw new RuntimeException("Unknown Subclass " + aClass);
    }

    public Package getOrAddPackage(String fullName) {
        String[] packageNameSplit = fullName.split("\\.");
        return getOrAddPackageRecursive(result, packageNameSplit);
    }

    public Class getOrAddClass(String fullName) {
        String[] classNameSplit = fullName.split("\\.");
        return getOrAddClassRecursive(result, classNameSplit);
    }

    private Class getOrAddClassRecursive(Element rootPackage, String[] className) {
        if(className.length == 1) {
            return getOrAddClass(rootPackage, className[0]);
        }
        Element nextPackage = getOrAddPackage(rootPackage, className[0]);
        String[] remainingClassName = Arrays.stream(className).skip(1).toArray(String[]::new);
        return getOrAddClassRecursive(nextPackage, remainingClassName);
    }

    private Class getOrAddClass(Element aPackage, String className) {
        return aPackage.children.stream()
                .filter(x -> x.getName().equals(className))
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .findFirst()
                .orElseGet(() -> {
                    Class result = new Class(className, aPackage);
                    aPackage.children.add(result);
                    return result;
                });
    }

    private Package getOrAddPackageRecursive(Element rootPackage, String[] packageName) {
        if(packageName.length == 1) {
            return getOrAddPackage(rootPackage, packageName[0]);
        }
        Element nextPackage = getOrAddPackage(rootPackage, packageName[0]);
        String[] remainingClassName = Arrays.stream(packageName).skip(1).toArray(String[]::new);
        return getOrAddPackageRecursive(nextPackage, remainingClassName);
    }

    private Package getOrAddPackage(Element rootPackage, String packageName) {
        return rootPackage.children.stream()
                .filter(x -> x.getName().equals(packageName))
                .filter(Package.class::isInstance)
                .map(Package.class::cast)
                .findFirst()
                .orElseGet(() -> {
                    Package result = new Package(packageName, rootPackage);
                    rootPackage.children.add(result);
                    return result;
                });
    }

}
