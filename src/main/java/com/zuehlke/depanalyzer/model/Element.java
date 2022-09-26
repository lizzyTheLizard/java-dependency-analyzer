package com.zuehlke.depanalyzer.model;

import java.util.Optional;
import java.util.stream.Stream;

public interface Element {
    Stream<Dependency> getDependencies();

    Stream<? extends Element> getChildren();

    Stream<Element> getChildrenRecursively();

    Optional<Element> getParent();

    Optional<String> getName();

    Optional<ClassDetails> getDetails();

    ElementType getType();

    void visit(Visitor v);

    <T> T apply(ElementMapper<T> mapper);

    Optional<String> getFullName();
}
