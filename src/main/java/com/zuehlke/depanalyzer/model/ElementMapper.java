package com.zuehlke.depanalyzer.model;

public interface ElementMapper<T> {
    T map(Element input);
}
