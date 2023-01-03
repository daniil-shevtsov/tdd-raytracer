package ray

import matrix.Matrix
import matrix.identityMatrix
import tuple.Point
import tuple.Vector
import tuple.point

data class Sphere(
    override val transform: Matrix = identityMatrix(),
    override val material: Material,
) : Intersectable {
    fun transformBy(transform: Matrix): Sphere {
        return copy(
            transform = transform
        )
    }
}

sealed interface Intersectable {
    val transform: Matrix
    val material: Material
}

fun Intersectable.normalAt(worldPoint: Point): Vector {
    val objectPoint = transform.inversed() * worldPoint
    val objectNormal = objectPoint - point(0.0,0.0,0.0)

    val worldNormal = (transform.inversed().transposed() * objectNormal)
        .copy(w = 0.0)

    return worldNormal.normalized
}

fun sphere(
    material: Material = material(),
) = Sphere(material =material)