package tuple.practice

import tuple.Point
import tuple.point
import kotlin.math.max

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

//while (true) {
//    Thread.sleep(500L)
//    projectile = tick(environment = environment, projectile = projectile)
//    println(projectile.position)
//    val field = visualize(
//        size = 11,
//        points = listOf(projectile.position.copy(y = 10.0 - projectile.position.y))
//    )
//    println("\r---------------")
//    println(field)
//}

fun generateVisualizations(
    pointStates: List<List<Point>>,
): List<String> {
    val positions = pointStates.flatten()
    val min = point(
        x = positions.minOf { it.x },
        y = positions.minOf { it.y },
        z = positions.minOf { it.z }
    )
    val max = point(
        x = positions.maxOf { it.x },
        y = positions.maxOf { it.y },
        z = positions.maxOf { it.z }
    )
    val size = max(
        max.x - min.x,
        max.y - min.y,
    ).toInt() + 1
    val origin = min

    return pointStates.map { points ->
        "\r--\n" + visualize(
            size = size,
            points = points
                .map { point ->
                    point.copy(
                        x = point.x - origin.x,
                        y = point.y - origin.y,
                    )
                }
                .map { point -> point.copy(y = size - 1 - point.y) }

        )
    }
}