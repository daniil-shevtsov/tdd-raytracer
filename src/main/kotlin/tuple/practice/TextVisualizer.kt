package tuple.practice

import tuple.Point

fun visualize(
    size: Int,
    default: Char = '#',
    pointChar: Char = '*',
    points: List<Point> = emptyList(),
): String {
    return (0 until size).joinToString(separator = "\n") { y ->
        (0 until size).joinToString(separator = "") { x ->
            val point = points.find { it.x.toInt() == x && it.y.toInt() == y }
            if (point != null) {
                pointChar.toString()
            } else {
                default.toString()
            }

        }
    }
}