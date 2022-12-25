package ray

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