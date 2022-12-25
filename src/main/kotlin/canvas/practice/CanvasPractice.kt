package canvas.practice

import canvas.canvas
import canvas.color.color
import canvas.toPmm

fun canvasPractice(): String {
    return canvas(width = 100, height = 100).let {
        it.copy(
            pixels = it.pixels.mapIndexed { y, row ->
                row.mapIndexed { x, color ->
                    val yFraction = 0.01
                    color(
                        red = 1.0 * (1 - yFraction * y),
                        green = 1.0 * (1 - yFraction * y),
                        blue = 1.0 * (1 - yFraction * y),
                    )
                }
            }
        ).toPmm()
    }
}