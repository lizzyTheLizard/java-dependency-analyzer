package com.zuehlke.depanalyzer.mapper;

import com.zuehlke.depanalyzer.graph.DelegatedElement;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementMapper;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class SimplifyPackageMapper implements ElementMapper<Element> {
    private final String packageName;

    @Override
    public Element map(Element input) {
        return new MappedElement(input, null);
    }

    private class MappedElement extends DelegatedElement {
        private final boolean isSelectedPackage;

        public MappedElement(Element element, Element parent) {
            super(element, parent);
            isSelectedPackage = getFullName().map(packageName::equals).orElse(false);
        }

        @Override
        public Stream<Dependency> getDependencies() {
            if (isSelectedPackage) {
                return getChildrenRecursively()
                        .flatMap(Element::getDependencies)
                        .map(this::replace);
            } else {
                return element.getDependencies().map(this::replace);
            }
        }

        private Dependency replace(Dependency d) {
            String newFrom = d.getFrom().startsWith(packageName) ? packageName : d.getFrom();
            String newTo = d.getTo().startsWith(packageName) ? packageName : d.getTo();
            return new Dependency(newFrom, newTo, d.getDetails());
        }

        @Override
        public Stream<Element> getChildren() {
            return isSelectedPackage ? Stream.of() : element.getChildren().map(c -> new MappedElement(c, this));
        }
    }
}
