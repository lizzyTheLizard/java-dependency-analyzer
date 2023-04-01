package site.gutschi.dependency.jdeps

import net.lingala.zip4j.ZipFile
import java.io.*
import java.nio.file.Files
import java.util.*
import java.util.stream.Stream

private const val TMP_FOLDER_PREFIX = "fat_jar_unzipped"

class FatJarReader @Throws(IOException::class) constructor(inputFile: File) {
    private val libFolder: File
    val isFatJar: Boolean

    init {
        val tmpFolder: File = Files.createTempDirectory(TMP_FOLDER_PREFIX).toFile()
        tmpFolder.deleteOnExit()
        ZipFile(inputFile).extractAll(tmpFolder.absolutePath)
        libFolder = tmpFolder.resolve("BOOT-INF").resolve("lib")
        isFatJar = libFolder.exists()
    }

    fun streamJars(): Stream<File> {
        return Arrays.stream(libFolder.listFiles())
    }
}