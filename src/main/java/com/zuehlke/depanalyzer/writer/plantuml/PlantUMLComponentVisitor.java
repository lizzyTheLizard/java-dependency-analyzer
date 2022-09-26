package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.Visitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class PlantUMLComponentVisitor implements Visitor {
    private final StringBuilder sb;

    @Override
    public void visitElement(Element element) {
        switch (element.getType()) {
            case GRAPH:
                break;
            case CLASS:
                sb.append(String.format("class \"%s\" as %s",
                        element.getName().orElseThrow(),
                        element.getFullName().orElseThrow()));
                sb.append(System.lineSeparator());
                break;
            case PACKAGE:
                sb.append(String.format("package \"%s\" as %s {",
                        element.getName().orElseThrow(),
                        element.getFullName().orElseThrow()));
                sb.append(System.lineSeparator());
                break;
        }
    }

    @Override
    public void exitElement(Element element) {
        switch (element.getType()) {
            case GRAPH:
            case CLASS:
                break;
            case PACKAGE:
                sb.append("}");
                sb.append(System.lineSeparator());
                break;
        }
    }
}
