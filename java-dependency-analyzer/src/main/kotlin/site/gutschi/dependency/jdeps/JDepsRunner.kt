package site.gutschi.dependency.jdeps

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.nio.file.Files
import java.util.*
import java.util.spi.ToolProvider
import java.util.stream.Collectors

class JDepsRunner {
    private val jdepsTool = ToolProvider
        .findFirst("jdeps")
        .orElseThrow { JDepsException.jdepsNotFound() }
    private val files: MutableList<File> = LinkedList()
    private var fatJarMatcher: (File) -> Boolean = { true }
    private var multiRelease: String? = null

    fun fatJarMatcher(fatJarMatcher: (File) -> Boolean): JDepsRunner {
        if (files.size > 0) {
            throw JDepsException.matcherAfterFile()
        }
        this.fatJarMatcher = fatJarMatcher
        return this
    }

    fun file(vararg files: File): JDepsRunner {
        Arrays.stream(files).forEach { e: File -> this.singleFile(e) }
        return this
    }

    private fun singleFile(file: File) {
        ensureTargetFileValid(file)
        files.add(file)
        if (file.isDirectory || !file.name.endsWith(".jar")) {
            return
        }
        try {
            val fatJarReader = FatJarReader(file)
            if (!fatJarReader.isFatJar) {
                return
            }
            fatJarReader.streamJars()
                .filter { t: File -> fatJarMatcher(t) }
                .forEach { e: File -> this.singleFile(e) }
        } catch (e: IOException) {
            throw JDepsException.cannotUnzip(e)
        }
    }

    fun multiRelease(multiRelease: String): JDepsRunner {
        this.multiRelease = multiRelease
        return this
    }

    fun run(): JDepsResult {
        val args = getArgs()
        try {
            StringWriter().use { stringWriter ->
                val result = jdepsTool.run(PrintWriter(stringWriter), PrintWriter(System.err), *args.toTypedArray())
                if (result != 0) {
                    throw JDepsException.nonZeroReturn(result, stringWriter.toString())
                }
                return stringWriter.toString().lines().stream()
                    .filter { i -> isOutputLine(i) }
                    .map { l -> JDepsResultLine.fromLine(l) }
                    .collect(Collectors.toList())
            }
        } catch (e: Exception) {
            throw JDepsException.wrap(e)
        }
    }

    private fun isOutputLine(line: String): Boolean {
        return line.startsWith(" ")
    }

    private fun getArgs(): List<String> {
        val args: MutableList<String> = LinkedList()
        if (multiRelease != null) {
            args.add("--multi-release")
            args.add(multiRelease!!)
        }
        args.add("-v")
        args.add("-q")
        files.stream()
            .map { obj: File -> obj.absolutePath }
            .forEach { e: String -> args.add(e) }
        return args
    }

    private fun ensureTargetFileValid(targetFile: File) {
        if (!targetFile.exists()) {
            throw JDepsException.targetFileDoesNotExist(targetFile)
        }
        if (!Files.isReadable(targetFile.toPath())) {
            throw JDepsException.targetFileNotReadable(targetFile)
        }
    }
}