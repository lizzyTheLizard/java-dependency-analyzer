package site.gutschi.dependency

import site.gutschi.dependency.frameworks.Framework
import site.gutschi.dependency.frameworks.JavaBase
import site.gutschi.dependency.frameworks.Spring
import java.io.File


data class Properties(
    val inputs: Collection<File>,
    val log: (message: String, level: Level) -> Unit = { m: String, l: Level -> println("$l: $m") },
    val outputFolder: File? = null,
    val fatJarMatchers: Collection<Regex> = listOf(),
    val includeFatJarClasses: Boolean = false,
    val basePackage: String? = null,
    val collapsePackages: Collection<String> = listOf(),
    val ignoredPackages: Collection<String> = listOf(),
    val frameworks: List<Framework> = listOf(JavaBase(), Spring())
) {
    enum class Level { DEBUG, INFO, WARN }
}
