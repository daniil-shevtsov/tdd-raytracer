package canvas

import canvas.color.Color
import canvas.color.color

data class Pixel(
    val x: Int,
    val y: Int,
)

data class Canvas(
    val width: Int,
    val height: Int,
    val pixels: List<List<Color>> = (0 until height).map {
        (0 until width).map {
            color(0.0, 0.0, 0.0)
        }
    },
)

fun Canvas.writePixel(
    x: Int,
    y: Int,
    color: Color
): Canvas {
    return copy(
        pixels = pixels.mapIndexed { row, colorRow ->
            colorRow.mapIndexed { column, currentColor ->
                when {
                    row == y && column == x -> color
                    else -> currentColor
                }
            }
        }
    )
}

fun Canvas.pixelAt(x: Int, y: Int): Color {
    return pixels[y][x]
}

fun canvas(
    width: Int,
    height: Int,
) = Canvas(
    width = width,
    height = height,
)