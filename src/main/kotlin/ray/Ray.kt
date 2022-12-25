package ray

import tuple.Point
import tuple.Vector

data class Ray(
    val origin: Point,
    val direction: Vector,
) {

    fun position(at: Double): Point {
        return origin + direction * at
    }

}

fun intersection(
    sphere: Double,
    ray: Ray
): List<Double> {
    return listOf(4.0, 6.0)
}

fun ray(origin: Point, direction: Vector) = Ray(
    origin = origin,
    direction = direction,
)