package site.gutschi.dependency.asm

import site.gutschi.dependency.Output.Dependency
import site.gutschi.dependency.Properties
import site.gutschi.dependency.TestFileHelpers.Companion.getFile
import site.gutschi.dependency.assertDependenciesEquals
import kotlin.test.Test

internal class DependencyVisitorTest {
    @Test
    fun classFile() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("DependencyVisitor.class")),
            attributeCollectors = listOf()
        )
        val results = analyze(properties)
        assertDependenciesEquals("DependencyVisitor.deps.json", results)
    }

    @Test
    fun flatJar() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("flat-demo-0.0.1-SNAPSHOT.jar")),
            attributeCollectors = listOf()
        )
        val results = analyze(properties)
        assertDependenciesEquals("flat-demo-0.0.1-SNAPSHOT.deps.json", results)
    }

    @Test
    fun fatJar() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("fat-demo-0.0.1-SNAPSHOT.jar")),
            attributeCollectors = listOf()
        )
        val results = analyze(properties)
        assertDependenciesEquals("fat-demo-0.0.1-SNAPSHOT.deps.json", results)
    }

    @Test
    fun fatJarWithBoot() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("fat-demo-0.0.1-SNAPSHOT.jar")),
            includeFatJarClasses = true,
            attributeCollectors = listOf()
        )
        val results = analyze(properties)
        assertDependenciesEquals("fat-demo-0.0.1-SNAPSHOT.deps-boot.json", results)
    }

    @Test
    fun fatJarWithDependency() {
        val properties = Properties(
            name = "name",
            version = "version",
            inputs = listOf(getFile("fat-demo-0.0.1-SNAPSHOT.jar")),
            fatJarMatchers = listOf(Regex("snakeyaml.*")),
            attributeCollectors = listOf()
        )
        val results = analyze(properties)
        assertDependenciesEquals("fat-demo-0.0.1-SNAPSHOT.deps-snake.json", results)
    }

    private fun analyze(properties: Properties): Collection<Dependency> {
        val classFileCollection = ClassFileCollection(properties)
        classFileCollection.apply()
        return classFileCollection.dependencies
    }
}
