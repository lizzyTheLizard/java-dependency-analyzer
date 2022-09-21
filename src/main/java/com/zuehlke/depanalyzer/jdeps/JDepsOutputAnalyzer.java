package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.graph.DependencyGraph;
import com.zuehlke.depanalyzer.graph.DependencyGraphBuilder;
import com.zuehlke.depanalyzer.graph.Element;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;

public class JDepsOutputAnalyzer extends PrintWriter {

    public JDepsOutputAnalyzer(){
        super(new StringWriter());
    }

    public DependencyGraph toOutput(){
        DependencyGraphBuilder resultBuilder = new DependencyGraphBuilder();
        String outputString = this.out.toString();
        Arrays.stream(outputString.split(System.lineSeparator()))
                .filter(l -> l.length() > 0)
                .filter(l -> l.startsWith("  "))
                .map(this::splitDependencyLine)
                .flatMap(Optional::stream)
                .forEach(d -> {
                    Element from = resultBuilder.getOrAddClass(d.from);
                    Element to = resultBuilder.getOrAddClass(d.to);
                    resultBuilder.addDependency(from,to);
                });
        return resultBuilder.build();
    }

    private Optional<DetectedDependency> splitDependencyLine(String line) {
        //Lines do have the form "   com.zuehlke.depanlyzer.Main                        -> com.zuehlke.depanlyzer.analyze.JDepsRunner         target"
        String[] parts = Arrays.stream(line.split(" ")).filter(l -> l.length() > 0).toArray(String[]::new);
        if(parts.length < 4){
            return Optional.empty();
        }
        return Optional.of(new DetectedDependency(parts[0], parts[2], parts[3]));
   }

   private record DetectedDependency(String from, String to, String type){}
}
