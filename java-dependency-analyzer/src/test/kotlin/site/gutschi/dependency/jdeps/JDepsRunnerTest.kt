package site.gutschi.dependency.jdeps

import site.gutschi.dependency.TestFileHelpers
import java.io.File
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class JDepsRunnerTest {
    @Test
    fun flatFile() {
        val result: String = JDepsRunner()
            .file(TestFileHelpers.FLAT_JAR)
            .run()
            .stream().map { x -> x.toString() }.collect(Collectors.joining(System.lineSeparator()))
        val expected = TestFileHelpers.getFileContent("/flat-demo-0.0.1-SNAPSHOT.jdeps")
        assertEquals(expected, result)
    }

    @Test
    fun fatFileNone() {
        val result: String = JDepsRunner()
            .fatJarMatcher { false }
            .file(TestFileHelpers.FAT_JAR)
            .run()
            .stream().map { x -> x.toString() }.collect(Collectors.joining(System.lineSeparator()))
        val expectedFlat = TestFileHelpers.getFileContent("/flat-demo-0.0.1-SNAPSHOT.jdeps")
        val expectedFat = TestFileHelpers.getFileContent("/fat-demo-0.0.1-SNAPSHOT.jdeps")
        val expected = "$expectedFlat${System.lineSeparator()}$expectedFat"
        assertEquals(expected, result)
    }

    @Test
    fun fatFileSome() {
        val result: String = JDepsRunner()
            .fatJarMatcher { x -> x.name.startsWith("jul-") }
            .file(TestFileHelpers.FAT_JAR)
            .multiRelease("11")
            .run()
            .stream().map { x -> x.toString() }.collect(Collectors.joining(System.lineSeparator()))
        val expectedFlat = TestFileHelpers.getFileContent("/flat-demo-0.0.1-SNAPSHOT.jdeps")
        val expectedFat = TestFileHelpers.getFileContent("/fat-demo-0.0.1-SNAPSHOT.jdeps")
        val expectedJul = TestFileHelpers.getFileContent("/jul-to-slf4j-2.0.7.jdeps")
        val expected = "$expectedFlat${System.lineSeparator()}$expectedFat${System.lineSeparator()}$expectedJul"
        assertEquals(expected, result)
    }

    @Test
    fun invalidFile() {
        val file = File("/no-existing.jar")
        assertFailsWith<JDepsException>(block = { JDepsRunner().file(file) })
    }
}