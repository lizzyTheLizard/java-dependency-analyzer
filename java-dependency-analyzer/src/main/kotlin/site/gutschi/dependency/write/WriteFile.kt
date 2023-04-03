package site.gutschi.dependency.write

import com.google.gson.Gson
import site.gutschi.dependency.jdeps.JDepsResult
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

private const val TEMPLATE_PATH = "/frontend/index.html"
private const val OUTPUT_FILE_NAME = "index.html"

fun writeFile(outputDir: File, jdepsOutput: JDepsResult) {
    val template: String = getTemplate()
    val output = convertToOutput(jdepsOutput)
    ensureDirExists(outputDir)
    writeToOutputDir(outputDir, merge(template, output))
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

private fun convertToOutput(jdepsOutput: JDepsResult): String {
    val nodes = jdepsOutput.stream()
        .flatMap { x -> Stream.of(x.from, x.to) }
        .distinct()
        .map { x -> Node(x.split(".").last(), x, listOf())}
        .collect(Collectors.toList())
    val dependenies = jdepsOutput.stream()
        .map{x -> Dependency(x.from, x.to)}
        .collect(Collectors.toList())
    val output = Output(nodes, dependenies)
    val gson = Gson()
    return gson.toJson(output)
}

private fun merge(template: String, output: String): String {
    val indexWithinVariable = template.indexOf("site.gutschi.dependency.maven.integrationtest.TestA")
    val indexLeadingQute = template.substring(0, indexWithinVariable).lastIndexOf('`')
    val indexTrailingQute = template.indexOf('`', indexWithinVariable)
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


private data class Attribute(val name: String, val value: String, val type: String)
private data class Node(val name: String, val fullName: String, val attributes: List<Attribute>)
private data class Dependency(val from: String, val to: String)
private data class Output(val nodes: List<Node>, val dependencies: List<Dependency>)
