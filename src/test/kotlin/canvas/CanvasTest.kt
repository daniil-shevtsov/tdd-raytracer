package canvas

import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import canvas.color.color
import org.junit.jupiter.api.Test

internal class CanvasTest {

    @Test
    fun `should create canvas`() {
        val canvas = canvas(10, 20)

        assertThat(canvas).all {
            prop(Canvas::width).isEqualTo(10)
            prop(Canvas::height).isEqualTo(20)
            prop(Canvas::pixels).each { color ->
                color.isEqualTo(color(0.0, 0.0, 0.0))
            }
        }
    }

}