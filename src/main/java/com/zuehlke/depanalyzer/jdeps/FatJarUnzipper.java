package com.zuehlke.depanalyzer.jdeps;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FatJarUnzipper {
    private final InputStream fileInputStream;

    public static Path unzip(InputStream fileInputStream) {
        FatJarUnzipper unzipper = new FatJarUnzipper(fileInputStream);
        return unzipper.unzip();
    }

    private Path unzip() {
        try {
            Path outputDir = Files.createTempDirectory("fat_jar_unzipper");
            outputDir.toFile().deleteOnExit();
            ZipInputStream zis = new ZipInputStream(fileInputStream);
            unzipAllEntries(zis, outputDir);
            return outputDir;
        } catch (IOException e) {
            throw JDepsException.cannotUnzip(e);
        }
    }

    private void unzipAllEntries(ZipInputStream zis, Path outputDir) throws IOException {
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            unzipFileEntry(zis, ze, outputDir);
            zis.closeEntry();
        }
    }

    private void unzipFileEntry(ZipInputStream zis, ZipEntry ze, Path outputDir) {
        if (ze.isDirectory()) {
            return;
        }
        String outputFileName = ze.getName();
        File outputFile = outputDir.resolve(outputFileName).toFile();
        createParentDir(outputFile);
        unzipEntry(zis, outputFile);
    }

    private void createParentDir(File outputFile) {
        File parent = outputFile.getParentFile();
        if (parent.isDirectory()) {
            return;
        }
        if (parent.exists()) {
            String message = String.format("Directory %s already exists but is not dir", parent.getAbsolutePath());
            throw JDepsException.cannotUnzip(message, null);
        }
        if (!parent.mkdirs()) {
            String message = String.format("Directory %s could not be created", parent.getAbsolutePath());
            throw JDepsException.cannotUnzip(message, null);
        }
    }

    private void unzipEntry(ZipInputStream zis, File outputFile) {
        byte[] buffer = new byte[1024];
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw JDepsException.cannotUnzip(e);
        }
    }
}
