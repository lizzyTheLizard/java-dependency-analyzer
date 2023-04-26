package site.gutschi.dependency

import java.io.File


data class Properties(
    val name: String,
    val version: String,
    val inputs: Collection<File>,
    val log: (message: String, level: Level) -> Unit = { m: String, l: Level -> println("$l: $m") },
    val outputFolder: File? = null,
    val fatJarMatchers: Collection<Regex> = listOf(),
    val includeFatJarClasses: Boolean = false,
    val basePackage: String? = null,
    val collapsePackages: Collection<String> = listOf(),
    val ignoredPackages: Collection<String> = listOf(),
    val splitPackages: Collection<String> = listOf(),
    val attributeCollectors: List<AttributeCollector>
) {
    enum class Level { DEBUG, INFO, WARN }
}
