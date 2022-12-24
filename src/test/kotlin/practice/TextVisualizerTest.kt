package practice

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class TextVisualizerTest {

    @Test
    fun `should draw field`() {
        val field = visualize(size = 3, default = '*')
        assertThat(field).isEqualTo("""***
            |***
            |***
        """.trimMargin())

    }

    private fun visualize(size: Int, default: Char): String {
        return """***
            |***
            |***
        """.trimMargin()
    }

}