package com.zuehlke.depanalyzer.mapper;

import com.zuehlke.depanalyzer.graph.DelegatedElement;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FilterPackageMapper implements ElementMapper<Element> {
    private final Function<String, Boolean> filter;

    public static FilterPackageMapper prefix(String... prefixes) {
        return new FilterPackageMapper(fullName -> Arrays.stream(prefixes)
                .anyMatch(prefix -> fullName.equals(prefix) || fullName.startsWith(prefix + ".")));
    }

    public static FilterPackageMapper doesNotContain(String... forbidden) {
        return new FilterPackageMapper(fullName -> Arrays.stream(forbidden)
                .anyMatch(fullName::contains));
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
                    .filter(d -> filter.apply(d.getFrom()))
                    .filter(d -> filter.apply(d.getTo()));
        }

        @Override
        public Stream<Element> getChildren() {
            return element.getChildren()
                    .filter(x -> x.getFullName().map(filter).orElse(false))
                    .map(c -> new MappedElement(c, this));
        }
    }
}
