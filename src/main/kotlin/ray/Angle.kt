package ray

typealias Degrees = Float
typealias Radians = Float

fun Radians.toDegrees(): Degrees {
    return 180f * this / Math.PI.toFloat()
}

fun Degrees.toRadians(): Radians {
    return Math.PI.toFloat() * this / 180f
}