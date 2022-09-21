package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.visitor.CopyVisitor;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public class RemoveElementsFilter {
    public static Filter startsWith(String prefix) {
        Function<Element, Boolean> filter = e -> e.getFullName().startsWith(prefix);
        return removeElements(filter);
    }

    public static Filter matches(String regex) {
        Function<Element, Boolean> filter = e -> e.getFullName().matches(regex);
        return removeElements(filter);
    }

    public static Filter allClasses() {
        Function<Element, Boolean> filter = Class.class::isInstance;
        return removeElements(filter);
    }

    public static Filter removeElements(Function<Element, Boolean> filter) {
        Function<Element, String> newName = Element::getFullName;
        return input -> {
            DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
            CopyVisitor copyVisitor = new CopyVisitor(resultBuilder, filter, newName);
            input.visit(copyVisitor);
            return resultBuilder.build();
        };
    }
}


