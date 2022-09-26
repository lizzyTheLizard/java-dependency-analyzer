package com.zuehlke.depanalyzer.jdeps;

import java.io.File;
import java.io.IOException;

public class JDepsException extends RuntimeException {
    private JDepsException(String message, Throwable e) {
        super(message, e);
    }

    static JDepsException runtimeError(String writerOutput) {
        return new JDepsException("Error while running jDeps: " + writerOutput, null);
    }

    static JDepsException targetFileDoesNotExist(File targetFile) {
        return new JDepsException("File " + targetFile.getAbsolutePath() + " does not exist", null);
    }

    static JDepsException targetFileNotReadable(File targetFile) {
        return new JDepsException("File " + targetFile.getAbsolutePath() + " is not readable", null);
    }

    static JDepsException wrap(Exception e) {
        if (e instanceof JDepsException) {
            return (JDepsException) e;
        }
        return new JDepsException("Error while running jDeps: " + e.getMessage(), e);
    }

    static JDepsException noFatJar(String fileName) {
        return new JDepsException(fileName + " is not a fat jar", null);
    }

    static JDepsException couldNotReadFatJar(IOException e) {
        return new JDepsException("Could not read fat jar", e);
    }

    public static JDepsException cannotUnzip(String message, Throwable cause) {
        return new JDepsException("Could not unzip fat jar: " + message, cause);
    }

    public static JDepsException cannotUnzip(Throwable cause) {
        return cannotUnzip(cause.getMessage(), cause);
    }
}
