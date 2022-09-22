package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.graph.Class;
import com.zuehlke.depanalyzer.graph.DependencyGraph;
import com.zuehlke.depanalyzer.graph.DependencyGraphBuilder;
import com.zuehlke.depanalyzer.graph.Element;
import lombok.RequiredArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

class JDepsOutputAnalyzer extends PrintWriter {

    public JDepsOutputAnalyzer(){
        super(new StringWriter());
    }

    public DependencyGraph toOutput(){
        DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
        String outputString = this.out.toString();
        Arrays.stream(outputString.split(System.lineSeparator()))
                .filter(l -> l.length() > 0)
                .map(this::splitDependencyLine)
                .flatMap(Optional::stream)
                .forEach(d -> {
                    Element from = resultBuilder.getOrAddElement(getFullName(d.from), Class.class);
                    Element to = resultBuilder.getOrAddElement(getFullName(d.to), Class.class);
                    resultBuilder.addDependency(from,to);
                });
        return resultBuilder.build();
    }

    private Stack<String> getFullName(String name) {
        String[] split = name.split("\\.");
        Stack<String> result = new Stack<>();
        result.addAll(List.of(split));
        return result;
    }

    private Optional<DetectedDependency> splitDependencyLine(String line) {
        //Lines do have the form "   com.zuehlke.depanlyzer.Main                        -> com.zuehlke.depanlyzer.analyze.JDepsRunner         target"
        String[] parts = Arrays.stream(line.split(" "))
                .filter(l -> l.length() > 0)
                .toArray(String[]::new);
        if(parts.length < 4){
            return Optional.empty();
        }
        return Optional.of(new DetectedDependency(parts[0], parts[2], parts[3]));
   }

   @RequiredArgsConstructor
   private static class DetectedDependency{
       private final String from;
       private final String to;
       private final String type;
   }
}
