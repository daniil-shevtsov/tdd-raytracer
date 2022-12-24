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
): List<String> = pointStates.map { points ->
   "\r--\n" + visualize(
        size = 2,
        points = points.map { point -> point.copy(y = 1.0 - point.y) }
   )
}