import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import org.junit.jupiter.api.Test

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
    }

    fun point(x: Double, y: Double, z: Double) = Tuple(x = x, y = y, z = z, w = 0.0)

    fun vector(x: Double, y: Double, z: Double) = Tuple(x = x, y = y, z = z, w = 1.0)

}