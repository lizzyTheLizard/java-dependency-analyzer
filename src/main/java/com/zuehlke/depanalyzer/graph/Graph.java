package com.zuehlke.depanalyzer.graph;

import com.zuehlke.depanalyzer.model.*;
import lombok.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Graph implements Element {
    @NonNull
    private final Set<Dependency> dependencies;
    @NonNull
    private final Set<ClassDetails> classDetails;
    @Getter
    @NonNull
    private final ElementType type;
    private final String name;
    private final Element parent;
    private final String fullName;

    public static Element create(Set<Dependency> dependencies, Set<ClassDetails> classDetails) {
        return Graph.builder()
                .classDetails(classDetails)
                .dependencies(dependencies)
                .type(ElementType.GRAPH)
                .build();
    }

    @Override
    public Optional<Element> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public Optional<String> getFullName() {
        return Optional.ofNullable(fullName);
    }

    @Override
    public Optional<ClassDetails> getDetails() {
        return type == ElementType.CLASS ? classDetails.stream().findFirst() : Optional.empty();
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
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Stream<Dependency> getDependencies() {
        return dependencies.stream()
                .filter(x -> x.getFrom().equals(fullName));
    }

    @Override
    public Stream<Element> getChildren() {
        if (type == ElementType.CLASS) {
            return Stream.of();
        }
        return classDetails.stream()
                .map(ClassDetails::getName)
                .map(this::getNextLevelName)
                .distinct()
                .map(this::createElement);
    }

    @Override
    public Stream<Element> getChildrenRecursively() {
        return getChildren().flatMap(x -> Stream.concat(Stream.of(x), x.getChildrenRecursively()));
    }

    private String getNextLevelName(String className) {
        int prefixLength = getFullName().map(String::length).map(x -> x + 1).orElse(0);
        String withoutCurrentName = className.substring(prefixLength);
        int firstDot = withoutCurrentName.indexOf(".");
        if (firstDot < 0) {
            return withoutCurrentName;
        }
        return withoutCurrentName.substring(0, firstDot);
    }

    private Element createElement(String nextLevelName) {
        String newFullName = getFullName()
                .map(x -> x + "." + nextLevelName)
                .orElse(nextLevelName);
        Set<ClassDetails> filteredClassNames = classDetails.stream()
                .filter(x -> getNextLevelName(x.getName()).equals(nextLevelName))
                .collect(Collectors.toSet());
        Set<Dependency> filteredDependencies = dependencies.stream()
                .filter(x -> getNextLevelName(x.getFrom()).equals(nextLevelName))
                .collect(Collectors.toSet());
        boolean isClass = filteredClassNames.stream()
                .map(ClassDetails::getName)
                .anyMatch(newFullName::equals);
        return Graph.builder()
                .parent(this)
                .classDetails(filteredClassNames)
                .dependencies(filteredDependencies)
                .type(isClass ? ElementType.CLASS : ElementType.PACKAGE)
                .fullName(newFullName)
                .name(nextLevelName)
                .build();
    }
}
