package com.zuehlke.depanalyzer.mapper;

import com.zuehlke.depanalyzer.graph.DelegatedElement;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementMapper;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class SelectPackageMapper implements ElementMapper<Element> {
    private final String packageName;

    private boolean isParentPackage(Element input) {
        return input.getFullName().map(packageName::startsWith).orElse(false);
    }

    private boolean isSubPackage(Element input) {
        return input.getFullName().map(n -> n.startsWith(packageName)).orElse(false);
    }

    @Override
    public Element map(Element input) {
        return new MappedElement(input, null);
    }

    private class MappedElement extends DelegatedElement {
        public MappedElement(Element element, Element parent) {
            super(element, parent);
        }

        @Override
        public Stream<Dependency> getDependencies() {
            return element.getDependencies()
                    .filter(d -> d.getFrom().startsWith(packageName))
                    .filter(d -> d.getTo().startsWith(packageName));
        }

        @Override
        public Stream<Element> getChildren() {
            return element.getChildren()
                    .filter(x -> isSubPackage(x) || isParentPackage(x))
                    .map(c -> new MappedElement(c, this));
        }
    }
}
