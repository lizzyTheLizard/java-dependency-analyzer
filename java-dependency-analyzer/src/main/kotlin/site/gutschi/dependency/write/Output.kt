package site.gutschi.dependency.write

enum class AttributeType { @Suppress("unused")
LINK, TEXT, NUMBER, BOOLEAN }

data class Attribute(val name: String, val value: String, val type: AttributeType)
data class Node(val name: String, val fullName: String, val attributes: Collection<Attribute>)
data class Dependency(val from: String, val to: String)
data class Output(val nodes: Collection<Node>, val dependencies: Collection<Dependency>)
