package com.zuehlke.depanalyzer.graph;

import java.util.HashSet;
import java.util.Set;

public class Package extends Element {
    Package(String name, Element parent) {
        super(name, parent);
    }

    public void visit(Visitor v){
        v.visitPackage(this);
        super.visit(v);
        v.exitPackage(this);
    }
}
