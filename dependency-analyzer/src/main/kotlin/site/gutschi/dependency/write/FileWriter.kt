package site.gutschi.dependency.write

import com.google.gson.Gson
import site.gutschi.dependency.Output
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

private const val TEMPLATE_PATH = "/frontend/index.html"
private const val OUTPUT_FILE_NAME = "index.html"

class FileWriter(private val outputDir: File) {
    private val gson = Gson()

    @Throws(WriteFileException::class)
    fun write(output: Output) {
        val template: String = getTemplate()
        val strOutput = gson.toJson(output)
        ensureDirExists(outputDir)
        writeToOutputDir(outputDir, merge(template, strOutput))
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


    private fun merge(template: String, output: String): String {
        val indexWithinVariable = template.indexOf("site.gutschi.dependency.maven.integrationtest.TestA")
        val indexLeadingQute = template.substring(0, indexWithinVariable).lastIndexOf('`')
        val indexTrailingQute = template.indexOf('`', indexWithinVariable)
        return template.substring(0, indexLeadingQute + 1) + output + template.substring(indexTrailingQute)
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
}