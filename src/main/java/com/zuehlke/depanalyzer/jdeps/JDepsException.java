package com.zuehlke.depanalyzer.jdeps;

import java.io.File;
import java.io.IOException;

public class JDepsException extends RuntimeException {
    private JDepsException(String message, Throwable e) {
        super(message,e );
    }

    static JDepsException writerOutput(String writerOutput) {
        return new JDepsException("Error while running jDeps: " + writerOutput, null);
    }

    static JDepsException targetFileDoesNotExist(File targetFile) {
        return new JDepsException("File "+ targetFile.getAbsolutePath() + " does not exist", null);
    }

    static JDepsException targetFileNotReadable(File targetFile) {
        return new JDepsException("File "+ targetFile.getAbsolutePath() + " is not readable", null);
    }

    static JDepsException wrap(Exception e) {
        if(e instanceof JDepsException) {
            return (JDepsException) e;
        }
        return new JDepsException("Error while running jDeps: " + e.getMessage(), e);
    }

    static JDepsException noFatJar(File file) {
        return new JDepsException(file.getAbsolutePath() + " is not a fat jar", null);
    }

    static JDepsException couldNotReadFatJar(File file, IOException e) {
        return new JDepsException("Could not read fat jar " + file.getAbsolutePath(), e);
    }

    public static JDepsException cannotUnzip(File file, String message, Throwable cause) {
        return new JDepsException("Could not unzip fat jar " + file.getAbsolutePath() + ": " + message, cause);
    }
}
