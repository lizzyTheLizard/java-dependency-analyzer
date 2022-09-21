package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.visitor.CopyVisitor;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.function.Function;

@UtilityClass
public class RemoveCommonRootFilter {
    public static Filter filter() {
        return input -> {
            Element commonRoot = getCommonRoot(input);
            DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
            int commonRootLength = commonRoot.getFullName().lastIndexOf(".") +1;
            Function<Element, Boolean> filter = e -> Boolean.FALSE;
            Function<Element, String> newName = e -> e.getFullName().substring(commonRootLength);
            CopyVisitor copyVisitor = new CopyVisitor(resultBuilder, filter, newName);
            commonRoot.visit(copyVisitor);
            return resultBuilder.build();
        };
    }

    private static Element getCommonRoot(Element rootElement) {
        Set<Element> children = rootElement.getChildren();
        if(children.size() == 1) {
            return getCommonRoot(children.stream().findFirst().orElseThrow());
        }
        return  rootElement;
    }
}
