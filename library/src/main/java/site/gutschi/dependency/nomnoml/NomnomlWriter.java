package site.gutschi.dependency.nomnoml;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NomnomlWriter {
    private final static String OUTPUT_FILE_NAME = "index.html";
    private final static String TEMPLATE_PATH = "/nomnuml/index.html";
    private File outputDir;
    private String jdepsOutput;

    private NomnomlWriter() {

    }

    public static NomnomlWriter create() {
        return new NomnomlWriter();
    }

    public NomnomlWriter outputDir(File outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public NomnomlWriter jdepsOutput(String jdepsOutput) {
        this.jdepsOutput = jdepsOutput;
        return this;
    }

    public void write() {
        if (jdepsOutput == null) {
            throw NomnumlException.missingParameter("jdepsOutput");
        }
        if (outputDir == null) {
            throw NomnumlException.missingParameter("outputDir");
        }
        String template = getTemplate();
        String output = merge(template, jdepsOutput);
        ensureOutputDirExists();
        writeToOutputDir(output);
    }

    private String getTemplate() {
        URL url = getClass().getResource(TEMPLATE_PATH);
        if (url == null) {
            throw NomnumlException.couldNotParseUrl(TEMPLATE_PATH);
        }
        try {
            return IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw NomnumlException.couldNotReadTemplate(url, e);
        }
    }

    private String merge(String template, String jdeps) {
        int indexWithinVariable = template.indexOf("classes -> java.base");
        int indexLeadingQute = template.substring(0, indexWithinVariable).lastIndexOf('`');
        int indexTrailingQute = template.indexOf('`', indexWithinVariable);
        return template.substring(0, indexLeadingQute + 2) + jdeps + template.substring(indexTrailingQute);
    }

    private void ensureOutputDirExists() {
        if (outputDir.exists() && outputDir.isDirectory()) {
            return;
        }
        if (!outputDir.mkdir()) {
            throw NomnumlException.couldNotCreateOutputDir(outputDir);
        }
    }

    private void writeToOutputDir(String output) {
        Path path = Paths.get(outputDir.getPath(), OUTPUT_FILE_NAME);
        try {
            Files.writeString(path, output);
        } catch (IOException e) {
            throw NomnumlException.couldNotWrite(path, e);
        }
    }
}
