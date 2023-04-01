package site.gutschi.dependency.write

import site.gutschi.dependency.jdeps.JDepsResult
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

private const val TEMPLATE_PATH = "/frontend/index.html"
private const val OUTPUT_FILE_NAME = "index.html"

fun writeFile(outputDir: File, jdepsOutput: JDepsResult) {
    val template: String = getTemplate()
    val output: String = merge(template, jdepsOutput)
    ensureDirExists(outputDir)
    writeToOutputDir(outputDir, output)
}

private fun getTemplate(): String {
    val url = WriteFileException::class.java.getResource(TEMPLATE_PATH)
        ?: throw WriteFileException.couldNotParseUrl(TEMPLATE_PATH)
    return try {
        url.readText()
    } catch (e: IOException) {
        throw WriteFileException.couldNotReadTemplate(url, e)
    }
}

private fun merge(template: String, jdeps: JDepsResult): String {
    val indexWithinVariable =
        template.indexOf("site.gutschi.dependency.maven.integrationtest.TestA -> java.lang.Object")
    val indexLeadingQute = template.substring(0, indexWithinVariable).lastIndexOf('`')
    val indexTrailingQute = template.indexOf('`', indexWithinVariable)
    val output = jdeps.stream().map { x -> "${x.from} -> ${x.to}" }.collect(Collectors.joining("\n"))
    return template.substring(0, indexLeadingQute + 2) + output + template.substring(indexTrailingQute)
}

private fun ensureDirExists(outputDir: File) {
    if (outputDir.exists() && outputDir.isDirectory) {
        return
    }
    if (!outputDir.mkdir()) {
        throw WriteFileException.couldNotCreateOutputDir(outputDir)
    }
}

private fun writeToOutputDir(outputDir: File, output: String) {
    val path = Paths.get(outputDir.path, OUTPUT_FILE_NAME)
    try {
        Files.writeString(path, output)
    } catch (e: IOException) {
        throw WriteFileException.couldNotWrite(path, e)
    }
}

