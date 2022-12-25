package canvas.color

import kotlin.math.abs

data class Color(
    val red: Double,
    val green: Double,
    val blue: Double,
) {
    //    private val tuple = point(
//        x = red,
//        y = green,
//        z = blue,
//    )
//
//    val red: Double
//        get() = tuple.x
//    val green: Double
//        get() = tuple.y
//    val blue: Double
//        get() = tuple.z

    override fun equals(other: Any?): Boolean {

        return when (other) {
            is Color -> abs(red - other.red) < EPSILON
                    && abs(green - other.green) < EPSILON
                    && abs(blue - other.blue) < EPSILON
            else -> super.equals(other)
        }
    }

    operator fun plus(other: Color): Color {
        return Color(
            red = red + other.red,
            green = green + other.green,
            blue = blue + other.blue,
        )
    }

    operator fun minus(other: Color): Color {
        return Color(
            red = red - other.red,
            green = green - other.green,
            blue = blue - other.blue,
        )
    }

    operator fun times(n: Double): Color {
        return Color(
            red = red * n,
            green = green * n,
            blue = blue * n,
        )
    }

    operator fun times(other: Color): Color {
        return Color(
            red = red * other.red,
            green = green * other.green,
            blue = blue * other.blue,
        )
    }

    private companion object {
        const val EPSILON = 0.00001
    }
}

fun color(
    red: Double,
    green: Double,
    blue: Double
) = Color(
    red = red,
    green = green,
    blue = blue,
)