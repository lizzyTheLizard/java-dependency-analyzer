package com.zuehlke.depanalyzer.model;

public interface Visitor {
    default void visitElement(Element element) {
    }

    default void exitElement(Element element) {
    }

}
