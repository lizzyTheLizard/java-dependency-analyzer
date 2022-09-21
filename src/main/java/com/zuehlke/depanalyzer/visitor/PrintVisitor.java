package com.zuehlke.depanalyzer.visitor;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.Element;
import com.zuehlke.depanalyzer.graph.Visitor;
import lombok.RequiredArgsConstructor;

import java.io.PrintStream;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PrintVisitor implements Visitor {
    private final PrintStream out;

    @Override
    public void visitClass(Class aClass) {
        String dependencies = aClass.getDependencies().stream()
                .map(Element::getFullName)
                .collect(Collectors.joining(", "));
        if (dependencies.length() == 0) {
            return;
        }
        String line = String.format("%s depends on %s", aClass.getFullName(), dependencies);
        out.println(line);
    }
}
