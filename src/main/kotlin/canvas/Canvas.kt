package canvas

import canvas.color.Color

data class Canvas(
    val width: Int,
    val height: Int,
) {
    val pixels: List<Color>
        get() = emptyList()
}

fun canvas(
    width: Int,
    height: Int,
) = Canvas(
    width = width,
    height = height,
)