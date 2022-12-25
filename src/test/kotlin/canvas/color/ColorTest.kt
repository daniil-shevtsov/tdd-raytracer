package canvas.color

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class ColorTest {

    @Test
    fun `should create color`() {
        val color = color(-0.5, 0.4, 1.7)
        assertThat(color).all {
            prop(Color::red).isEqualTo(-0.5)
            prop(Color::green).isEqualTo(0.4)
            prop(Color::blue).isEqualTo(1.7)
        }
    }

    @Test
    fun `should add colors`() {
        val a = color(0.9, 0.6, 0.75)
        val b = color(0.7, 0.1, 0.25)
        assertThat(a + b).isEqualTo(color(1.6, 0.7, 1.0))
    }

    @Test
    fun `should subtract colors`() {
        val a = color(0.9, 0.6, 0.75)
        val b = color(0.7, 0.1, 0.25)
        assertThat(a - b).isEqualTo(color(0.2, 0.5, 0.5))
    }

    @Test
    fun `should multiply color by a scalar`() {
        val color = color(0.2, 0.3, 0.4)

        assertThat(color * 2.0).isEqualTo(color(0.4, 0.6, 0.8))
    }

    @Test
    fun `should mltiply color by color`() {
        val a = color(1.0, 0.2, 0.4)
        val b = color(0.9, 1.0, 0.1)

        assertThat(a * b).isEqualTo(color(0.9, 0.2, 0.04))
    }

}