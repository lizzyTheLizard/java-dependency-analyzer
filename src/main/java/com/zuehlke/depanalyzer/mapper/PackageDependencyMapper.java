package com.zuehlke.depanalyzer.mapper;

import com.zuehlke.depanalyzer.graph.DelegatedElement;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementMapper;
import com.zuehlke.depanalyzer.model.ElementType;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class PackageDependencyMapper implements ElementMapper<Element> {
    private final boolean removeClasses;

    @Override
    public Element map(Element input) {
        return new MappedElement(input, input.getParent().orElse(null));
    }

    private class MappedElement extends DelegatedElement {
        private final String parentName;
        private final String currentName;

        public MappedElement(Element element, Element parent) {
            super(element, parent);
            parentName = getParent().flatMap(Element::getFullName).orElse("");
            currentName = getFullName().orElse("");
        }

        @Override
        public Stream<Dependency> getDependencies() {
            Stream<Dependency> samePackageDependencies = element.getDependencies()
                    .filter(this::pointsToSameOrSubPackage)
                    .filter(this::pointsToSame);
            Stream<Dependency> subPackageDependencies = element.getDependencies()
                    .filter(this::pointsToSameOrSubPackage)
                    .filter(x -> !pointsToSame(x))
                    .map(d -> new Dependency(currentName, getNewToName(d, parentName), d.getDetails()));
            Stream<Dependency> directDependencies = Stream.concat(samePackageDependencies, subPackageDependencies);

            Stream<Dependency> childrenDependencies = getChildrenRecursively()
                    .flatMap(Element::getDependencies)
                    .filter(this::pointsToSameOrSubPackage)
                    .filter(this::pointsNotToItselfOrSubPackage)
                    .map(d -> new Dependency(currentName, getNewToName(d, parentName), d.getDetails()));
            return Stream.concat(directDependencies, childrenDependencies).distinct();
        }

        private boolean pointsToSameOrSubPackage(Dependency d) {
            return d.getTo().startsWith(parentName);
        }

        private boolean pointsToSame(Dependency d) {
            return !d.getTo().substring(parentName.length() + 1).contains(".");
        }

        private boolean pointsNotToItselfOrSubPackage(Dependency d) {
            return !d.getTo().startsWith(currentName);
        }

        private String getNewToName(Dependency d, String parentName) {
            int prefixLength = parentName.equals("") ? 0 : (parentName.length() + 1);
            String withoutCurrentName = d.getTo().substring(prefixLength);
            int firstDot = withoutCurrentName.indexOf(".");
            if (firstDot < 0) {
                return d.getTo();
            }
            String result = d.getTo().substring(0, prefixLength + firstDot);
            if (result.endsWith("mode")) {
                System.out.println();
            }
            return result;
        }

        @Override
        public Stream<Element> getChildren() {
            return element.getChildren()
                    .filter(x -> !x.getType().equals(ElementType.CLASS) || !removeClasses)
                    .map(c -> new MappedElement(c, this));
        }
    }
}
