package com.zuehlke.depanalyzer.graph;

@FunctionalInterface
public interface Filter {
    DependencyGraph apply(DependencyGraph input);
}
