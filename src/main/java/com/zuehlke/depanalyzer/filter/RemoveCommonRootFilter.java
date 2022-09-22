package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.visitor.CopyVisitor;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings("unused")
public class RemoveCommonRootFilter {
    public static Filter filter() {
        return input -> {
            Element commonRoot = getCommonRoot(input);
            DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
            Function<Element, Boolean> filter = e -> Boolean.FALSE;
            Function<Element, Stack<String>> newName = e -> newName(e, commonRoot);
            CopyVisitor copyVisitor = new CopyVisitor(resultBuilder, filter, newName);
            commonRoot.visit(copyVisitor);
            return resultBuilder.build();
        };
    }

    private static Stack<String> newName(Element e, Element commonRoot){
        Stack<String> result = new Stack<>();
        String commonRootName = commonRoot.getFullNameAsString();
        result.add(commonRootName);
        if(e.getFullName().equals(commonRoot.getFullName())){
            return result;
        }
        int commonRootLength = commonRoot.getFullName() == null ? 0 : commonRootName.length() + 1;
        String[] split = e.getFullNameAsString().substring(commonRootLength).split("\\.");
        result.addAll(Arrays.asList(split));
        return result;
    }

    private static Element getCommonRoot(Element rootElement) {
        Set<Element> children = rootElement.getChildren();
        if(children.size() == 1) {
            return getCommonRoot(children.stream().findFirst().orElseThrow());
        }
        return  rootElement;
    }
}
