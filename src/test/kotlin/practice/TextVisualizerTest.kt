package practice

import Point
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import point

class TextVisualizerTest {

    @Test
    fun `should draw 2x2 field`() {
        val field = visualize(size = 2, default = '*')
        assertThat(field).isEqualTo(
            """**
            |**
        """.trimMargin()
        )
    }

    @Test
    fun `should draw 3x3 field`() {
        val field = visualize(size = 3, default = '*')
        assertThat(field).isEqualTo(
            """***
            |***
            |***
        """.trimMargin()
        )
    }

    @Test
    fun `should draw different symbol`() {
        val field = visualize(size = 2, default = '#')
        assertThat(field).isEqualTo(
            """##
            |##
        """.trimMargin()
        )
    }

    @Test
    fun `should draw point`() {
        val field = visualize(
            size = 2,
            points = listOf(point(0.0, 0.0, 0.0))
        )
        assertThat(field).isEqualTo(
            """*#
            |##
        """.trimMargin()
        )
    }

    private fun visualize(
        size: Int,
        default: Char = '#',
        pointChar: Char = '*',
        points: List<Point> = emptyList(),
    ): String {
        return (0 until size).joinToString(separator = "\n") { y ->
            (0 until size).joinToString(separator = "") { x ->
                val point = points.find { it.x.toInt() == x && it.y.toInt() == y }
                if (point != null) {
                    pointChar.toString()
                } else {
                    default.toString()
                }

            }
        }
    }

}