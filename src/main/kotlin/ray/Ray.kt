package ray

import matrix.Matrix
import tuple.Point
import tuple.Vector
import tuple.point
import world.World
import kotlin.math.sqrt

data class Ray(
    val origin: Point,
    val direction: Vector,
) {

    fun position(at: Double): Point {
        return origin + direction * at
    }

    fun transformBy(translation: Matrix): Ray {
        return copy(
            origin = translation * origin,
            direction = translation * direction,
        )
    }

}

fun Ray.intersect(intersectable: Intersectable): Intersections {
    return intersection(intersectable, this)
}

fun Ray.intersect(world: World): Intersections {
    return world.objects.flatMap { intersectable ->
        intersect(intersectable)
    }.sortedBy { it.t }
}

fun intersection(
    sphere: Intersectable,
    ray: Ray
): Intersections {
    val ray = ray.transformBy(sphere.transform.inversed())
    val sphereToRay = ray.origin - point(0.0, 0.0, 0.0) // sphere at world origin right now
    val a = ray.direction dot ray.direction
    val b = 2 * (ray.direction dot sphereToRay)
    val c = (sphereToRay dot sphereToRay) - 1.0
    val discriminant = b * b - 4 * a * c

    if (discriminant < 0) {
        return emptyList()
    }
    val t1 = (-b - sqrt(discriminant)) / (2 * a)
    val t2 = (-b + sqrt(discriminant)) / (2 * a)

    return intersections(
        intersection(t = t1, intersected = sphere),
        intersection(t = t2, intersected = sphere),
    )
}

fun ray(origin: Point, direction: Vector) = Ray(
    origin = origin,
    direction = direction,
)