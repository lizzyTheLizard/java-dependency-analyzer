package site.gutschi.dependency

data class Output(
    val nodes: Collection<Node>,
    val dependencies: Collection<Dependency>,
    val properties: OutputProperties
) {
    @Suppress("unused")
    enum class AttributeType {
        LINK, TEXT, NUMBER, BOOLEAN
    }

    data class Attribute(val name: String, val value: String, val type: AttributeType)
    data class Node(val name: String, val fullName: String, val attributes: Collection<Attribute>)
    data class Dependency(val from: String, val to: String)
    data class OutputProperties(
        val name: String,
        val version: String,
        val basePackage: String? = null,
        val showClasses: String = "HIDE_INNER",
        val collapsePackages: Collection<String> = listOf(),
        val ignoredPackages: Collection<String> = listOf(),
        val splitPackages: Collection<String> = listOf(),
    )
}
