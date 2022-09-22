package com.zuehlke.depanalyzer.jdeps;

import com.zuehlke.depanalyzer.graph.DependencyGraph;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.spi.ToolProvider;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JDepsRunner {
    private final ToolProvider jdepsTool = ToolProvider.findFirst("jdeps").orElseThrow();
    private final List<File> files = new LinkedList<>();
    private String multiRelease = null;
    private boolean ignoreMissingDeps  = true;
    private boolean verbose  = true;

    public static JDepsRunner create() {
        return new JDepsRunner();
    }

    public JDepsRunner file(File ...files) {
        List<File> filesList = Arrays.asList(files);
        filesList.forEach(this::ensureTargetFileValid);
        this.files.addAll(filesList);
        return this;
    }

    public JDepsRunner fatJar(File file, Function<File, Boolean> matcher) {
        ensureTargetFileValid(file);
        try {
            Path tempFolder = Files.createTempDirectory("dependency_analyze");
            FatJarUnzipper.unzip(file, tempFolder);
            File libFolder = tempFolder.resolve("BOOT-INF").resolve("lib").toFile();
            if(!libFolder.exists()){
                throw JDepsException.noFatJar(file);
            }
            files.add(file);
            File[] libFiles = libFolder.listFiles();
            if(libFiles == null){
                return this;
            }
            Arrays.stream(libFiles).filter(matcher::apply).forEach(this.files::add);
            return this;
        } catch (IOException e) {
            throw JDepsException.couldNotReadFatJar(file, e);
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

    public DependencyGraph run(){
        try(JDepsOutputAnalyzer outWriter = new JDepsOutputAnalyzer(); JDepsErrorAnalyzer  errorWriter = new JDepsErrorAnalyzer()) {
            List<String> args = new LinkedList<>();
            if(multiRelease != null){
                args.add("--multi-release");
                args.add(multiRelease);
            }
            if(ignoreMissingDeps){
                args.add("--ignore-missing-deps");
            }
            if(verbose){
                args.add("-v");
            }
            files.stream().map(File::getAbsolutePath).forEach(args::add);
            jdepsTool.run(outWriter, errorWriter, args.toArray(String[]::new));
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
