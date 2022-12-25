package ray

data class Sphere(val garage: String = "") : Intersectable
sealed interface Intersectable

fun sphere() = Sphere()