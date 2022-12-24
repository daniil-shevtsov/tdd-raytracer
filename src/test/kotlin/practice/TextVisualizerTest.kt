package practice

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

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

    private fun visualize(size: Int, default: Char): String {
        return (0 until size).joinToString(separator = "\n") {
            (0 until size).joinToString(separator = "") {
                "*"
            }
        }
    }

}