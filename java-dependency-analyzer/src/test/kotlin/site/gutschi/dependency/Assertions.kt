package site.gutschi.dependency

import com.google.gson.GsonBuilder
import org.junit.ComparisonFailure
import site.gutschi.dependency.write.Dependency
import site.gutschi.dependency.write.Node
import site.gutschi.dependency.write.Output

private val gson = GsonBuilder().setPrettyPrinting().create()

fun assertDependenciesEquals(expectedFile: String, actual: Set<Dependency>) {
    val expectedOut = getOutputFromFile(expectedFile)
    val expected = expectedOut.dependencies
    val actualStr = gson.toJson(actual)
    val expectedStr = gson.toJson(expected)
    actual.forEach { a ->
        if (expected.none { e -> a == e }) throw ComparisonFailure(
            "$a not expected",
            expectedStr,
            actualStr
        )
    }
    expected.forEach { e ->
        if (actual.none { a -> e == a }) throw ComparisonFailure(
            "$e not present",
            expectedStr,
            actualStr
        )
    }
}

fun assertNodesEquals(expectedFile: String, actual: Set<Node>) {
    val expectedOut = getOutputFromFile(expectedFile)
    val expected = expectedOut.nodes
    val actualStr = gson.toJson(actual)
    val expectedStr = gson.toJson(expected)
    actual.forEach { a ->
        if (expected.none { e -> nodeEquals(e, a) }) throw ComparisonFailure(
            "$a not expected",
            expectedStr,
            actualStr
        )
    }
    expected.forEach { e ->
        if (actual.none { a -> nodeEquals(e, a) }) throw ComparisonFailure(
            "$e not present",
            expectedStr,
            actualStr
        )
    }
}

private fun nodeEquals(expected: Node, actual: Node): Boolean {
    if (expected.name != actual.name) {
        return false
    }
    if (expected.fullName != actual.fullName) {
        return false
    }
    if (actual.attributes.any { a -> expected.attributes.none { e -> a == e } }) {
        return false
    }
    return !expected.attributes.any { e -> actual.attributes.none { a -> a == e } }
}


private fun getOutputFromFile(expectedFile: String): Output {
    val expectedFileContent = TestFileHelpers.getFileContent(expectedFile)
    return runCatching { gson.fromJson(expectedFileContent, Output::class.java) }
        .getOrElse<Output, Output> {
            throw AssertionError("Cannot parse $expectedFile: $it.message")
        }
}
