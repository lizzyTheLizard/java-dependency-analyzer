package com.zuehlke.depanalyzer.graph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Element {
    final Set<Element> children = new HashSet<>();
    final Set<Element> dependencies = new HashSet<>();
    @Getter
    private final String name;
    @Getter
    protected final Element parent;

    public Set<Element> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Set<Element> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }

    public Stack<String> getFullName() {
        Stack<String> result = parent == null ? new Stack<>() : parent.getFullName();
        if(name != null){
            result.push(name);
        }
        return result;
    }

    public String getFullNameAsString() {
        return String.join(".", getFullName());
    }

    public void visit(Visitor v){
        children.forEach(c -> c.visit(v));
    }
}
