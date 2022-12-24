package canvas

import assertk.all
import assertk.assertThat
import assertk.assertions.each
import assertk.assertions.index
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
            prop(Canvas::pixels).all {
                each { colorRow ->
                    colorRow.each { color ->
                        color.isEqualTo(color(0.0, 0.0, 0.0))
                    }
                }
            }
        }
    }

    @Test
    fun `should draw pixel at canvas`() {
        val canvas = canvas(10, 20)
        val red = color(1.0, 0.0, 0.0)
        val updatedCanvas = canvas.writePixel(
            x = 2,
            y = 3,
            color = red,
        )

        assertThat(updatedCanvas)
            .prop(Canvas::pixels)
            .index(3)
            .index(2)
            .isEqualTo(red)
    }

    @Test
    fun `should reada pixel from canvas`() {
        val canvas = canvas(10, 20)
        val red = color(1.0, 0.0, 0.0)
        val updatedCanvas = canvas.writePixel(
            x = 2,
            y = 3,
            color = red,
        )

        assertThat(updatedCanvas.pixelAt(x = 2, y = 3))
            .isEqualTo(red)
    }

    @Test
    fun `creates PMM header`() {
        val canvas = canvas(5, 3)

        val pmm = canvas.toPmm()

        assertThat(pmm)
            .transform { it.split("\n").take(3).joinToString(separator = "\n") }
            .isEqualTo(
                """
                P3
                5 3
                255
            """.trimIndent()
            )
    }

    @Test
    fun `creates PMM pixel data`() {
        val canvas = canvas(5, 3)
            .let { it.writePixel(x = 0, y = 0, color = color(1.5, 0.0, 0.0)) }
            .let { it.writePixel(x = 2, y = 1, color = color(0.0, 0.5, 0.0)) }
            .let { it.writePixel(x = 4, y = 2, color = color(-0.5, 0.0, 1.0)) }

        val pmm = canvas.toPmm()

        assertThat(pmm)
            .transform { it.substringAfter("255") }
            .isEqualTo(
                """255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                    0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
                    0 0 0 0 0 0 0 0 0 0 0 0 0 0 255""".trimIndent()
            )
    }

}