package ray

@JvmInline
value class Degrees(val raw: Double)

@JvmInline
value class Radians(val raw: Double)

fun degrees(raw: Double) = Degrees(raw)
fun radians(raw: Double) = Radians(raw)

val Double.deg: Degrees
    get() = degrees(this)
val Double.rad: Radians
    get() = radians(this)

fun Radians.toDegrees(): Degrees {
    return degrees(180.0 * raw / Math.PI)
}

fun Degrees.toRadians(): Radians {
    return radians(Math.PI * raw / 180.0)
}