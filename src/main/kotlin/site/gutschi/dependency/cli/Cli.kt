package site.gutschi.dependency.cli

import kotlinx.cli.*
import site.gutschi.dependency.Main
import site.gutschi.dependency.Properties
import site.gutschi.dependency.frameworks.JavaBase
import site.gutschi.dependency.frameworks.Spring
import java.io.File

fun main(args: Array<String>) {
    val parser = ArgParser("java-dependency-analyzer")
    val debug by parser.option(
        ArgType.Boolean,
        shortName = "d",
        fullName = "debug",
        description = "Turn on debug mode"
    ).default(false)
    val outputFolder by parser.option(
        ArgType.String,
        shortName = "o",
        fullName = "outputFolder",
        description = "Output folder name"
    ).default(System.getProperty("user.dir"))
    val includeFatJarClasses by parser.option(
        ArgType.Boolean,
        fullName = "includeFatJarClasses",
        description = "When input is a SpringBoot FatJar, include the SpringBoot-Loader-Classes as well"
    ).default(false)
    val fatJarMatchers by parser.option(
        ArgType.String,
        fullName = "fatJarMatchers",
        description = "When input is a SpringBoot FatJar, include all dependencies that matches on of those regexes"
    ).multiple()
    val inputs by parser.argument(
        ArgType.String,
        description = "Input file or folder"
    ).vararg()
    parser.parse(args)

    val properties = Properties(
        name = inputs.joinToString(", "),
        version = "0",
        log = { m, l -> log(m, l, debug) },
        outputFolder = File(outputFolder),
        inputs = inputs.map { File(it) },
        includeFatJarClasses = includeFatJarClasses,
        fatJarMatchers = fatJarMatchers.map { Regex(it) },
        attributeCollectors = listOf(JavaBase(), Spring())
    )
    Main(properties).execute()
}

fun log(m: String, l: Properties.Level, debug: Boolean) {
    when (l) {
        Properties.Level.DEBUG -> if (debug) println(m)
        Properties.Level.INFO -> println(m)
        Properties.Level.WARN -> System.err.println(m)
    }
}