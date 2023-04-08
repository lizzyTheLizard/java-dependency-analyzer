package site.gutschi.dependency

import java.io.File

enum class Level { DEBUG, INFO, WARN }

typealias Logger = (message: String, level: Level) -> Unit

data class Properties(
    val inputs: Collection<File>,
    val log: Logger = { m: String, l: Level -> println("$l: $m") },
    val outputFolder: File? = null,
    val fatJarMatchers: Collection<Regex> = listOf(),
    val includeFatJarClasses: Boolean = false,
)
