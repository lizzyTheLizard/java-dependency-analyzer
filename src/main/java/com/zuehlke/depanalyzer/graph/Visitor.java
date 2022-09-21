package com.zuehlke.depanalyzer.graph;

public interface Visitor {
    default void visitClass(Class aClass){}

    default void visitPackage(Package aPackage){}

    default void exitPackage(Package aPackage){}

}
