package com.zuehlke.depanalyzer.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Dependency {
    private final String from;
    private final String to;
    private final DependencyDetails details;

    public static Dependency create(String fromClass, String toCLass) {
        return new Dependency(
                fromClass,
                toCLass,
                new DependencyDetails(fromClass, toCLass)
        );
    }


}