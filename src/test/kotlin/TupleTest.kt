import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs

class TupleTest {

    @Test
    fun `tuple with w=1 is a point`() {
        val tuple = tuple(x = 4.3, y = -4.2, z = 3.1, w = 1.0)

        assertThat(tuple)
            .all {
                prop(Tuple::x).isEqualTo(4.3)
                prop(Tuple::y).isEqualTo(-4.2)
                prop(Tuple::z).isEqualTo(3.1)
                prop(Tuple::w).isEqualTo(1.0)
                prop(Tuple::isPoint).isTrue()
                prop(Tuple::isVector).isFalse()
            }
    }

    @Test
    fun `tuple with w=0 is a vector`() {
        val tuple = tuple(x = 4.3, y = -4.2, z = 3.1, w = 0.0)

        assertThat(tuple)
            .all {
                prop(Tuple::x).isEqualTo(4.3)
                prop(Tuple::y).isEqualTo(-4.2)
                prop(Tuple::z).isEqualTo(3.1)
                prop(Tuple::w).isEqualTo(0.0)
                prop(Tuple::isVector).isTrue()
                prop(Tuple::isPoint).isFalse()
            }
    }

    @Test
    fun `should create point with factory function`() {
        val point = point(x = 1.0, y = 2.0, z = 3.0)
        assertThat(point)
            .isEqualTo(tuple(x = 1.0, y = 2.0, z = 3.0, w = 0.0))
    }

    @Test
    fun `should create vector with factory function`() {
        val point = vector(x = 1.0, y = 2.0, z = 3.0)
        assertThat(point)
            .isEqualTo(tuple(x = 1.0, y = 2.0, z = 3.0, w = 1.0))
    }

    @Test
    fun `two tuples should be different when difference in digit before epsilon`() {
        val a = point(x = 1.0001, y = 2.0, z = 3.0)
        val b = point(x = 1.0002, y = 2.0, z = 3.0)

        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `two tuples should be equal when difference in digit after epsilon`() {
        val a = point(x = 1.00001, y = 2.0, z = 3.0)
        val b = point(x = 1.00002, y = 2.0, z = 3.0)

        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should add two tuples`() {
        val a = tuple(x = 3.0, y = -2.0, z = 5.0, w = 1.0)
        val b = tuple(x = -2.0, y = 3.0, z = 1.0, w = 0.0)

        val sum = a + b

        assertThat(sum).isEqualTo(tuple(x = 1.0, y = 1.0, z = 6.0, w = 1.0))
    }

    @Test
    fun `should subtract two tuples`() {
        val a = point(x = 3.0, y = 2.0, z = 1.0)
        val b = point(x = 5.0, y = 6.0, z = 7.0)

        val difference = a - b

        assertThat(difference).isEqualTo(vector(x = -2.0, y = -4.0, z = -6.0))
    }


    private fun tuple(
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0,
        w: Double = 0.0,
    ) = Tuple(
        x = x,
        y = y,
        z = z,
        w = w,
    )

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

        override fun equals(other: Any?): Boolean {

            return when (other) {
                is Tuple -> abs(x - other.x) < EPSILON
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

        private companion object {
            const val EPSILON = 0.00001
        }
    }

    fun point(x: Double, y: Double, z: Double) = Tuple(x = x, y = y, z = z, w = 0.0)

    fun vector(x: Double, y: Double, z: Double) = Tuple(x = x, y = y, z = z, w = 1.0)

}