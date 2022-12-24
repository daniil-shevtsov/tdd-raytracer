package tuple.practice

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import tuple.point

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

    @Test
    fun `should draw two points`() {
        val field = visualize(
            size = 2,
            points = listOf(point(0.0, 0.0, 0.0),point(1.0, 1.0, 0.0))
        )
        assertThat(field).isEqualTo(
            """*#
            |#*
        """.trimMargin()
        )
    }

}