package com.zuehlke.depanalyzer.writer.plantuml;

import com.zuehlke.depanalyzer.model.Element;
import com.zuehlke.depanalyzer.model.ElementMapper;

public class PlantUMLWriter implements ElementMapper<String> {

    @Override
    public String map(Element input) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml");
        sb.append(System.lineSeparator());
        sb.append("skinparam linetype ortho");
        sb.append(System.lineSeparator());
        input.visit(new PlantUMLComponentVisitor(sb));
        input.visit(new PlantUMLDependencyVisitor(sb));
        sb.append("@enduml");
        return sb.toString();
    }
}
