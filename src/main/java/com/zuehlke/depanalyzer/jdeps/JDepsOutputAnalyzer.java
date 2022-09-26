package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.graph.Graph;
import com.zuehlke.depanalyzer.model.ClassDetails;
import com.zuehlke.depanalyzer.model.Dependency;
import com.zuehlke.depanalyzer.model.DependencyDetails;
import com.zuehlke.depanalyzer.model.Element;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JDepsOutputAnalyzer extends PrintWriter {

    public JDepsOutputAnalyzer() {
        super(new StringWriter());
    }

    public Element toOutput() {
        String outputString = this.out.toString();
        List<DependencyDetails> detectedDependencies = getDetectedDependencies(outputString);
        Set<ClassDetails> classes = detectedDependencies.stream()
                .flatMap(d -> Stream.of(d.getFromClass(), d.getToClass()))
                .distinct()
                .map(ClassDetails::new)
                .collect(Collectors.toSet());
        Set<Dependency> dependencies = detectedDependencies.stream()
                .map(d -> new Dependency(d.getFromClass(), d.getToClass(), d))
                .collect(Collectors.toSet());
        return Graph.create(dependencies, classes);
    }

    private List<DependencyDetails> getDetectedDependencies(String outputString) {
        return Arrays.stream(outputString.split(System.lineSeparator()))
                .filter(l -> l.length() > 0)
                .map(this::splitDependencyLine)
                .flatMap(Optional::stream)
                .toList();
    }


    private Optional<DependencyDetails> splitDependencyLine(String line) {
        if (line.startsWith("Error")) {
            throw JDepsException.runtimeError(line);
        }
        if (!line.contains("->")) {
            System.out.println("Ignored jdeps output line: " + line);
            return Optional.empty();
        }
        if (line.contains("-> not found")) {
            System.out.println("Ignored jdeps output line: " + line);
            return Optional.empty();
        }
        String[] parts = Arrays.stream(line.split(" "))
                .filter(l -> l.length() > 0)
                .toArray(String[]::new);
        if (parts[0].startsWith("not") || parts[2].startsWith("not")) {
            System.out.println("OutputLine: " + line);
            return Optional.empty();
        }
        if (parts.length < 4) {
            return Optional.empty();
        }
        return Optional.of(new DependencyDetails(parts[0], parts[2]));
    }
}
