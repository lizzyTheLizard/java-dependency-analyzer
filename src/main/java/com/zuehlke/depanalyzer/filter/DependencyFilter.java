package com.zuehlke.depanalyzer.filter;

import com.zuehlke.depanalyzer.graph.*;
import com.zuehlke.depanalyzer.visitor.UpdateDependencyCopyVisitor;
import lombok.experimental.UtilityClass;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import com.zuehlke.depanalyzer.writer.plantuml.PlantUMLWriter;

@UtilityClass
public class DependencyFilter {
    public static Filter usePackageDependencies(){
        return input -> {
            DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
            UsePackageDependencies addDependency = new UsePackageDependencies(resultBuilder);
            UpdateDependencyCopyVisitor copyVisitor = new UpdateDependencyCopyVisitor(resultBuilder, addDependency);
            input.visit(copyVisitor);
            return resultBuilder.build();
        };
    }

    private record UsePackageDependencies(DependencyGraphBuilder resultBuilder) implements BiConsumer<Element, Element> {
        @Override
        public void accept(Element from, Element to) {
            if (from.getParent() == to.getParent()) {
                resultBuilder.addDependency(from, to);
                return;
            }
            new PlantUMLWriter(null);
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
