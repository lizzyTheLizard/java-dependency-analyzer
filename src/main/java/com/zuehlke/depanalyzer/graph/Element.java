package com.zuehlke.depanalyzer.graph;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

    public String getFullName() {
        if(parent == null) {
            return name;
        }
        String parentName = parent.getFullName();
        if(parentName == null) {
            return name;
        }
        return parentName + "." + name;
    }

    public void visit(Visitor v){
        children.forEach(c -> c.visit(v));
    }
}
