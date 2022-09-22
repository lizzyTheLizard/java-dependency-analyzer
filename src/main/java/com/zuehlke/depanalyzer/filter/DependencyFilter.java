package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.visitor.UpdateDependencyCopyVisitor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

@UtilityClass
@SuppressWarnings("unused")
public class DependencyFilter {
    public static Filter usePackageDependencies(boolean removeClasses){
        return input -> filter(removeClasses, input);
    }

    private static DependencyGraph filter(boolean removeClasses, DependencyGraph input){
        DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
        UsePackageDependencies addDependency = new UsePackageDependencies(resultBuilder);
        UpdateDependencyCopyVisitor copyVisitor = new UpdateDependencyCopyVisitor(resultBuilder, addDependency);
        input.visit(copyVisitor);
        DependencyGraph result = resultBuilder.build();
        if(removeClasses){
            result = result.filter(RemoveElementsFilter.removeElements(Class.class::isInstance));
        }
        return result;
    }

    @RequiredArgsConstructor
    private class UsePackageDependencies implements BiConsumer<Element, Element> {
        private final DependencyGraphBuilder resultBuilder;

        @Override
        public void accept(Element from, Element to) {
            if (from.getParent() == to.getParent()) {
                resultBuilder.addDependency(from, to);
                return;
            }
            Element commonParent = findCommonParent(from, to);
            Element packageFrom = findParentWithParent(from, commonParent);
            Element packageTo = findParentWithParent(to, commonParent);
            resultBuilder.addDependency(packageFrom, packageTo);
        }

        private Element findCommonParent(Element e1, Element e2) {
                List<Element> ancestors1 = getAllAncestors(e1);
                List<Element> ancestor2 = getAllAncestors(e2);
                return ancestor2.stream()
                        .filter(ancestors1::contains)
                        .findFirst()
                        .orElseThrow();
            }

        private List<Element> getAllAncestors(Element element) {
            List<Element> result = new LinkedList<>();
            for (
                    Element currentAncestor = element;
                    currentAncestor != null;
                    currentAncestor = currentAncestor.getParent()
            ) {
                result.add(currentAncestor);
            }
            return result;
        }

        private Element findParentWithParent(Element e, Element parent) {
            if (e.getParent() == parent) {
                return e;
            }
            return findParentWithParent(e.getParent(), parent);
        }
    }
}
