package com.zuehlke.depanalyzer.jdeps;

import java.io.PrintWriter;
import java.io.StringWriter;

public class JDepsErrorAnalyzer extends PrintWriter {

    public JDepsErrorAnalyzer(){
        super(new StringWriter());
    }

    public boolean hadError(){
        String errorString = this.out.toString();
        return errorString.length() > 0;
    }

    public String errorDetails(){
        return this.out.toString();
    }
}
