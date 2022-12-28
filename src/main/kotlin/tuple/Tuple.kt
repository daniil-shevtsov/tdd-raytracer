package tuple

import ray.*
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt

data class Tuple(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double,
) {
    val isPoint: Boolean
        get() = w == 1.0
    val isVector: Boolean
        get() = w == 0.0

    val magnitude: Double
        get() = sqrt(x * x + y * y + z * z + w * w)

    val normalized: Tuple
        get() = this / magnitude

    override fun equals(other: Any?): Boolean {

        return when (other) {
            is Tuple -> abs(x - other.x) < EPSILON
                    && abs(y - other.y) < EPSILON
                    && abs(z - other.z) < EPSILON
                    && abs(w - other.w) < EPSILON
            else -> super.equals(other)
        }
    }

    operator fun plus(other: Tuple): Tuple {
        return Tuple(
            x = x + other.x,
            y = y + other.y,
            z = z + other.z,
            w = w + other.w,
        )
    }

    operator fun minus(other: Tuple): Tuple {
        return Tuple(
            x = x - other.x,
            y = y - other.y,
            z = z - other.z,
            w = w - other.w,
        )
    }

    operator fun unaryMinus(): Tuple {
        return Tuple(
            x = -x,
            y = -y,
            z = -z,
            w = -w,
        )
    }

    operator fun times(other: Double): Tuple {
        return Tuple(
            x = x * other,
            y = y * other,
            z = z * other,
            w = w * other,
        )
    }

    operator fun div(other: Double): Tuple {
        return this * (1.0 / other)
    }

    infix fun dot(other: Tuple): Double {
        return x * other.x + y * other.y + z * other.z + w * other.w
    }

    infix fun cross(other: Tuple): Tuple {
        return vector(
            x = y * other.z - z * other.y,
            y = z * other.x - x * other.z,
            z = x * other.y - y * other.x,
        )
    }

    fun reflectFrom(normal: Tuple): Vector {
        return this - normal * 2.0 * dot(normal)
    }

    fun angleBetween(vector: Vector): Radians {
        val angle = acos(this.normalized dot vector.normalized) / (normalized.magnitude * vector.normalized.magnitude)
        val radians = radians(angle)
        val degrees = radians.toDegrees()
        return radians(
            when {
                this == -vector -> radians.raw
                x == 1.0 && vector.y == 1.0 -> radians.raw
                vector.y == 10.0 -> radians.raw
                else -> radians.raw
            }
        )
    }

    private companion object {
        const val EPSILON = 0.00001
    }
}
typealias Point = Tuple
typealias Vector = Tuple

fun point(
    x: Double,
    y: Double,
    z: Double
) = Tuple(x = x, y = y, z = z, w = 1.0)

fun point(
    x: Int,
    y: Int,
    z: Int,
) = point(
    x = x.toDouble(),
    y = y.toDouble(),
    z = z.toDouble(),
)

fun vector(
    x: Double,
    y: Double,
    z: Double,
) = Tuple(x = x, y = y, z = z, w = 0.0)

fun vector(
    x: Int,
    y: Int,
    z: Int,
) = vector(
    x = x.toDouble(),
    y = y.toDouble(),
    z = z.toDouble(),
)

fun vector(
    x: Radians,
    y: Radians,
    z: Radians,
) = Tuple(x = x.raw, y = y.raw, z = z.raw, w = 0.0)

fun vector(
    x: Degrees,
    y: Degrees,
    z: Degrees,
) = Tuple(x = x.toRadians().raw, y = y.toRadians().raw, z = z.toRadians().raw, w = 0.0)
