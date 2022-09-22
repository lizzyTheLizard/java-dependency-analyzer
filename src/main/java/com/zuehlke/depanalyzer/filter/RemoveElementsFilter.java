package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.visitor.CopyVisitor;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
@SuppressWarnings("unused")
public class RemoveElementsFilter {
    public static Filter startsWith(String prefix) {
        Function<Element, Boolean> filter = e -> e.getFullNameAsString().startsWith(prefix);
        return removeElements(filter);
    }

    public static Filter notStartsWith(String prefix) {
        Function<Element, Boolean> filter = e -> !e.getFullNameAsString().startsWith(prefix);
        return removeElements(filter);
    }

    public static Filter matches(String regex) {
        Function<Element, Boolean> filter = e -> e.getFullNameAsString().matches(regex);
        return removeElements(filter);
    }

    public static Filter removeElements(Function<Element, Boolean> filter) {
        return input -> {
            DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
            CopyVisitor copyVisitor = new CopyVisitor(resultBuilder, filter, Element::getFullName);
            input.visit(copyVisitor);
            return resultBuilder.build();
        };
    }
}


