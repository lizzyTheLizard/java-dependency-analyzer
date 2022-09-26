package com.zuehlke.depanalyzer.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class DependencyDetails {
    private final String fromClass;
    private final String toClass;
}
