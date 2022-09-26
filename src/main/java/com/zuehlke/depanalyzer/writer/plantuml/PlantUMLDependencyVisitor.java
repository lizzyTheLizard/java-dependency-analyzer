package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.Visitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class PlantUMLDependencyVisitor implements Visitor {
    private final StringBuilder sb;

    @Override
    public void visitElement(Element element) {
        element.getDependencies().forEach(d -> {
            sb.append(String.format("%s --> %s", d.getFrom(), d.getTo()));
            sb.append(System.lineSeparator());
        });
    }
}
