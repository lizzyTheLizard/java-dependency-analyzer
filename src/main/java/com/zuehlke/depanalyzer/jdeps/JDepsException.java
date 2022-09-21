package com.zuehlke.depanalyzer.jdeps;

import java.io.File;

public class JDepsException extends RuntimeException {
    private JDepsException(String message, Throwable e) {
        super(message,e );
    }

    public static JDepsException writerOutput(String writerOutput) {
        return new JDepsException("Error while running jDeps: " + writerOutput, null);
    }

    public static JDepsException targetFileDoesNotExist(File targetFile) {
        return new JDepsException("File "+ targetFile.getAbsolutePath() + " does not exist", null);
    }

    public static JDepsException targetFileNotReadable(File targetFile) {
        return new JDepsException("File "+ targetFile.getAbsolutePath() + " is not readable", null);
    }

    public static JDepsException wrap(Exception e) {
        if(e instanceof JDepsException) {
            return (JDepsException) e;
        }
        return new JDepsException("Error while running jDeps: " + e.getMessage(), e);
    }
}
