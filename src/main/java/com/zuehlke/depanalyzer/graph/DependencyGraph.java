package com.zuehlke.depanalyzer.graph;

public class DependencyGraph extends Element {
    DependencyGraph() {
        super(null, null);
    }

    public DependencyGraph filter(Filter f) {
        return f.apply(this);
    }

    public void write(Writer writer){
        writer.write(this);
    }
}
