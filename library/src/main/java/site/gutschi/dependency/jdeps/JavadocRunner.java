package site.gutschi.dependency.jdeps;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.spi.ToolProvider;

public class JavadocRunner {
    private final ToolProvider jdepsTool = ToolProvider.findFirst("javadoc").orElseThrow();
    private final List<File> files = new LinkedList<>();

    private JavadocRunner() {

    }

    public static JavadocRunner create() {
        return new JavadocRunner();
    }

    public JavadocRunner file(File... files) {
        Arrays.stream(files)
                .peek(this::ensureTargetFileValid)
                .forEach(this.files::add);
        return this;
    }

    public JavadocRunner fatJar(String fileName, Function<File, Boolean> matcher) {
        File file = new File(fileName);
        file(file);
        try (FileInputStream fis = new FileInputStream(file)) {
            Path tempFolder = FatJarUnzipper.unzip(fis);
            File libFolder = tempFolder.resolve("BOOT-INF").resolve("lib").toFile();
            if (!libFolder.exists()) {
                throw JDepsException.noFatJar(fileName);
            }
            Optional.of(libFolder)
                    .map(File::listFiles)
                    .stream()
                    .flatMap(Arrays::stream)
                    .filter(matcher::apply)
                    .forEach(this.files::add);
            return this;
        } catch (IOException e) {
            throw JDepsException.couldNotReadFatJar(e);
        }
    }

    public String run() {
        List<String> args = new LinkedList<>();
        files.stream().map(File::getAbsolutePath).forEach(args::add);
        try (StringWriter stringWriter = new StringWriter()) {
            jdepsTool.run(new PrintWriter(stringWriter), new PrintWriter(System.err), args.toArray(String[]::new));
            return stringWriter.toString();
        } catch (Exception e) {
            throw JDepsException.wrap(e);
        }
    }

    private void ensureTargetFileValid(File targetFile) {
        if (!targetFile.exists()) {
            throw JDepsException.targetFileDoesNotExist(targetFile);
        }
        if (!Files.isReadable(targetFile.toPath())) {
            throw JDepsException.targetFileNotReadable(targetFile);
        }
    }
}