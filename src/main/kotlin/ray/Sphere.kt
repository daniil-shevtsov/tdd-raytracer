package ray

import matrix.Matrix
import matrix.identityMatrix

data class Sphere(override val transform: Matrix = identityMatrix()) : Intersectable {
    fun transformBy(transform: Matrix): Sphere {
        return copy(
            transform = transform
        )
    }
}

sealed interface Intersectable {
    val transform: Matrix
}

fun sphere() = Sphere()