package com.zuehlke.depanalyzer.graph;

import com.zuehlke.depanalyzer.model.*;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class DelegatedElement implements Element {
    protected final Element element;
    private final Element parent;

    @Override
    public Stream<Dependency> getDependencies() {
        return element.getDependencies();
    }

    @Override
    public abstract Stream<Element> getChildren();

    @Override
    public Stream<Element> getChildrenRecursively() {
        return getChildren()
                .flatMap(x -> Stream.concat(Stream.of(x), x.getChildrenRecursively()));
    }

    @Override
    public Optional<Element> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Optional<String> getName() {
        return element.getName();
    }

    @Override
    public Optional<ClassDetails> getDetails() {
        return element.getDetails();
    }

    @Override
    public ElementType getType() {
        return element.getType();
    }

    @Override
    public void visit(Visitor v) {
        v.visitElement(this);
        getChildren().forEach(x -> x.visit(v));
        v.exitElement(this);
    }

    @Override
    public <T> T apply(ElementMapper<T> mapper) {
        return mapper.map(this);
    }

    @Override
    public Optional<String> getFullName() {
        return element.getFullName();
    }
}
