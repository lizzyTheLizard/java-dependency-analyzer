package com.zuehlke.depanalyzer.graph;

import lombok.*;

import java.util.*;

public class Class extends Element {
    Class(String name, Element parent) {
        super(name, parent);
    }

    public void visit(Visitor v){
        v.visitClass(this);
    }
}
