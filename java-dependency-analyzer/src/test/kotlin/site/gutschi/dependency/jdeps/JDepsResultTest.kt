package site.gutschi.dependency.jdeps

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class JDepsResultTest {
    @Test
    fun invalidLine() {
        val input = "invalid"
        assertFailsWith<JDepsException>(block = { JDepsResultLine.fromLine(input) })
    }

    @Test
    fun validLine() {
        val input = "A -> B C"
        val result = JDepsResultLine.fromLine(input)
        assertEquals("A", result.from)
        assertEquals("B", result.to)
        assertEquals("C", result.module)
    }
}