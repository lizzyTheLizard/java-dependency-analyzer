package site.gutschi.dependency.nomnoml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class NomnumlException extends RuntimeException {
    private NomnumlException(String message, Throwable e) {
        super(message, e);
    }

    static NomnumlException couldNotReadTemplate(String path) {
        return new NomnumlException("Could not read template '" + path + "'", null);
    }

    static NomnumlException couldNotReadTemplate(URL url, Throwable e) {
        return new NomnumlException("Could not read template '" + url.getPath() + "'", e);
    }

    public static NomnumlException couldNotCreateOutputDir(File f) {
        return new NomnumlException("Could not create output dir '" + f.getAbsolutePath() + "'", null);
    }

    public static NomnumlException couldNotWrite(Path path, IOException e) {
        return new NomnumlException("Could not write to output file '" + path.toAbsolutePath() + "'", e);
    }

    public static NomnumlException missingParameter(String parameter) {
        return new NomnumlException("'" + parameter + "' not set", null);
    }
}