package site.gutschi.dependency.jdeps

import site.gutschi.dependency.TestFileHelpers
import java.io.File
import java.io.IOException
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class FatJarReaderTest {
    @Test
    fun flatJar() {
        val fatJarReader = FatJarReader(TestFileHelpers.FLAT_JAR)
        assertTrue(!fatJarReader.isFatJar)
    }

    @Test
    fun invalidFile() {
        val file = File("/no-existing.jar")
        assertFailsWith<IOException>(block = { FatJarReader(file) })
    }

    @Test
    fun fatJar() {
        val fatJarReader = FatJarReader(TestFileHelpers.FAT_JAR)
        assertTrue(fatJarReader.isFatJar)
        val results = fatJarReader.streamJars().map { x -> x.name }.collect(Collectors.toList())
        assertTrue(results.contains("jakarta.annotation-api-2.1.1.jar"))
        assertTrue(results.contains("jul-to-slf4j-2.0.7.jar"))
        assertEquals(17, results.size)
    }
}