package canvas

import canvas.color.Color
import canvas.color.color
import kotlin.math.roundToInt

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

fun Canvas.toPmm(): String {
    val prefix = "P3"
    val pixelSize = "$width $height"
    val colorRange = 255

    val header = """$prefix
        |$pixelSize
        |$colorRange
            """.trimMargin()
    val pixelData = pixels
        .mapIndexed { y, colorRow ->
            colorRow.mapIndexed { x, color ->
                listOf(
                    color.red.clampToRangeAndRound(),
                    color.green.clampToRangeAndRound(),
                    color.blue.clampToRangeAndRound(),
                ).joinToString(separator = " ")
            }.joinToString(separator = " ")
        }.joinToString(separator = "\n") { line ->
            when {
                line.length >= 70 -> {
                    val lastSpaceBeforeLimit = line.substring(startIndex = 0, endIndex = 71).lastIndexOf(' ')
                    listOf(
                        line.substring(startIndex = 0, endIndex = lastSpaceBeforeLimit),
                        line.substring(startIndex = lastSpaceBeforeLimit+1, endIndex = line.length)
                    ).joinToString(separator = "\n")
                }
                else -> line
            }
        }

    return header + "\n" + pixelData + "\n"
}

private fun Double.clampToRangeAndRound() = (this * 255)
    .coerceAtLeast(0.0)
    .coerceAtMost(255.0)
    .roundToInt()

fun canvas(
    width: Int,
    height: Int,
) = Canvas(
    width = width,
    height = height,
)