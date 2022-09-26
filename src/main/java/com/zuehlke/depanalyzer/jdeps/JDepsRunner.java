package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.model.Element;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.spi.ToolProvider;

@SuppressWarnings({"unused", "UnusedReturnValue"})
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JDepsRunner {
    private final ToolProvider jdepsTool = ToolProvider.findFirst("jdeps").orElseThrow();
    private final List<File> files = new LinkedList<>();
    private String multiRelease = null;
    private boolean ignoreMissingDeps = true;
    private boolean verbose = true;

    public static JDepsRunner create() {
        return new JDepsRunner();
    }

    public JDepsRunner file(String... fileNames) {
        Arrays.stream(fileNames)
                .map(File::new)
                .peek(this::ensureTargetFileValid)
                .forEach(this.files::add);
        return this;
    }

    public JDepsRunner fatJar(String fileName, Function<File, Boolean> matcher) {
        file(fileName);
        try (FileInputStream fis = new FileInputStream(fileName)) {
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

    public JDepsRunner multiRelease(String multiRelease) {
        this.multiRelease = multiRelease;
        return this;
    }

    public JDepsRunner verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public JDepsRunner ignoreMissingDeps(boolean ignoreMissingDeps) {
        this.ignoreMissingDeps = ignoreMissingDeps;
        return this;
    }

    public Element run() {
        try (JDepsOutputAnalyzer outWriter = new JDepsOutputAnalyzer()) {
            List<String> args = new LinkedList<>();
            if (multiRelease != null) {
                args.add("--multi-release");
                args.add(multiRelease);
            }
            if (ignoreMissingDeps) {
                args.add("--ignore-missing-deps");
            }
            if (verbose) {
                args.add("-v");
            }
            files.stream().map(File::getAbsolutePath).forEach(args::add);
            jdepsTool.run(outWriter, new PrintWriter(System.err), args.toArray(String[]::new));
            return outWriter.toOutput();
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
