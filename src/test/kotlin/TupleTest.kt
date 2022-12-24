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
        val tuple = tuple(4.3, -4.2, 3.1, 1.0)

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
        val tuple = tuple(4.3, -4.2, 3.1, 0.0)

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

}