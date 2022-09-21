package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.graph.DependencyGraph;

import java.io.File;
import java.nio.file.Files;
import java.util.spi.ToolProvider;

public class JDepsRunner {
    private final ToolProvider jdepsTool;

    public JDepsRunner(){
        jdepsTool = ToolProvider.findFirst("jdeps").orElseThrow();
    }

    public DependencyGraph run(File targetFile){
        ensureTargetFileValid(targetFile);
        try(JDepsOutputAnalyzer outWriter = new JDepsOutputAnalyzer(); JDepsErrorAnalyzer  errorWriter = new JDepsErrorAnalyzer()) {
            jdepsTool.run(outWriter, errorWriter, "-v", targetFile.getAbsolutePath());
            ensureNoErrorOutput(errorWriter);
            return outWriter.toOutput();
        } catch (Exception e) {
            throw JDepsException.wrap(e);
        }
    }

    private void ensureTargetFileValid(File targetFile){
        if(!targetFile.exists()) {
            throw JDepsException.targetFileDoesNotExist(targetFile);
        }
        if(!Files.isReadable(targetFile.toPath())) {
            throw JDepsException.targetFileNotReadable(targetFile);
        }
    }

    private void ensureNoErrorOutput(JDepsErrorAnalyzer errorAnalyzer){
        if(errorAnalyzer.hadError()) {
            throw JDepsException.writerOutput(errorAnalyzer.errorDetails());
        }
    }
}
