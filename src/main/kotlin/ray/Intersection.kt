package ray

import tuple.Point
import tuple.Vector

data class Intersection(
    val t: Double,
    val intersected: Intersectable,
)

fun intersection(
    t: Double,
    intersected: Intersectable,
) = Intersection(
    t = t,
    intersected = intersected,
)
typealias Intersections = List<Intersection>

fun intersections(vararg intersections: Intersection): Intersections = intersections.toList()

fun hit(intersections: Intersections): Intersection? {
    return intersections
        .sortedBy { it.t }
        .firstOrNull { it.t > 0 }
}

data class IntersectionState(
    val t: Double,
    val intersected: Intersectable,
    val point: Point,
    val eye: Vector,
    val normal: Vector,
    val inside: Boolean,
)

fun Intersection.prepareState(ray: Ray): IntersectionState {
    val point = ray.origin + ray.direction * t
    val eye = -ray.direction
    val assumedNormal = intersected.normalAt(point)
    val inside = assumedNormal.dot(eye) < 0
    val finalNormal = when {
        inside -> -assumedNormal
        else -> assumedNormal
    }

    return IntersectionState(
        t = t,
        intersected = intersected,
        point = point,
        eye = eye,
        normal = finalNormal,
        inside = inside,
    )
}