package com.zuehlke.depanalyzer.jdeps;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@UtilityClass
class FatJarUnzipper {
    public static void unzip(File zipFile, Path dir) {
        try (FileInputStream fis = new FileInputStream(zipFile)) {
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze;
            while((ze = zis.getNextEntry()) != null){
                File newFile = dir.resolve(ze.getName()).toFile();
                if(ze.isDirectory()) {
                    continue;
                }
                File parent = newFile.getParentFile();
                if(parent.exists() && !parent.isDirectory()) {
                    throw JDepsException.cannotUnzip(zipFile, "Directory " + parent.getAbsolutePath() + " already exists but is not dir", null);
                }
                if(!parent.exists() && !parent.mkdirs()) {
                    throw JDepsException.cannotUnzip(zipFile, "Directory " + parent.getAbsolutePath() + " could not be crated", null);
                }
                try(FileOutputStream fos = new FileOutputStream(newFile)){
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            throw JDepsException.cannotUnzip(zipFile, e.getMessage(), e);
        }
    }

}
