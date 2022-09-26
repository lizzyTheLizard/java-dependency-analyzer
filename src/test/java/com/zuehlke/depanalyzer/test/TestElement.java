package com.zuehlke.depanalyzer.test;

import com.zuehlke.depanalyzer.model.*;
import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestElement implements Element {
    private final String name;
    private final String fullName;
    @Getter
    private final ElementType type;
    private final ClassDetails details;
    private final List<Dependency> dependencies;
    private final List<TestElement> children;
    @Setter(AccessLevel.PRIVATE)
    private Element parent;

    @Builder(toBuilder = true)
    private TestElement(Element parent, String name, String fullName,
                        @NonNull ElementType type,
                        @Singular List<Dependency> dependencies,
                        @Singular List<TestElement> children,
                        ClassDetails details) {
        this.parent = parent;
        this.name = name;
        this.fullName = fullName;
        this.type = type;
        this.dependencies = dependencies;
        this.children = children;
        this.children.forEach(t -> t.setParent(this));
        this.details = details;
    }

    @Override
    public Stream<Dependency> getDependencies() {
        return dependencies.stream();
    }

    @Override
    public Stream<TestElement> getChildren() {
        return children.stream();
    }

    @Override
    public Stream<Element> getChildrenRecursively() {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Optional<Element> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<String> getFullName() {
        return Optional.ofNullable(fullName);
    }

    @Override
    public Optional<ClassDetails> getDetails() {
        return Optional.ofNullable(details);
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

    public TestElement copy() {
        List<TestElement> copiedChildren = children.stream()
                .map(TestElement::copy)
                .collect(Collectors.toList());
        return new TestElement(parent, name, fullName, type, dependencies, copiedChildren, details);
    }
}
