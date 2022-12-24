import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class TupleTest {

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
            .isEqualTo(tuple(x = 1.0, y = 2.0, z = 3.0, w = 1.0))
    }

    @Test
    fun `should create vector with factory function`() {
        val point = vector(x = 1.0, y = 2.0, z = 3.0)
        assertThat(point)
            .isEqualTo(tuple(x = 1.0, y = 2.0, z = 3.0, w = 0.0))
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
    fun `should subtract two points`() {
        val a = point(x = 3.0, y = 2.0, z = 1.0)
        val b = point(x = 5.0, y = 6.0, z = 7.0)

        val difference = a - b

        assertThat(difference).isEqualTo(vector(x = -2.0, y = -4.0, z = -6.0))
    }

    @Test
    fun `should subtract vector from a point`() {
        val a = point(x = 3.0, y = 2.0, z = 1.0)
        val b = vector(x = 5.0, y = 6.0, z = 7.0)

        val difference = a - b

        assertThat(difference).isEqualTo(point(x = -2.0, y = -4.0, z = -6.0))
    }

    @Test
    fun `should subtract two vectors`() {
        val a = vector(x = 3.0, y = 2.0, z = 1.0)
        val b = vector(x = 5.0, y = 6.0, z = 7.0)

        val difference = a - b

        assertThat(difference).isEqualTo(vector(x = -2.0, y = -4.0, z = -6.0))
    }

    @Test
    fun `should subtract vector from a zero vector`() {
        val a = vector(x = 0.0, y = 0.0, z = 0.0)
        val b = vector(x = 1.0, y = -2.0, z = 3.0)

        val difference = a - b

        assertThat(difference).isEqualTo(vector(x = -1.0, y = 2.0, z = -3.0))
    }

    @Test
    fun `should negate a tuple`() {
        val tuple = tuple(x = 1.0, y = -2.0, z = 3.0, w = -4.0)

        val negated = -tuple

        assertThat(negated).isEqualTo(tuple(x = -1.0, y = 2.0, z = -3.0, w = 4.0))
    }

    @Test
    fun `should multiply a tuple by a scalar`() {
        val tuple = tuple(x = 1.0, y = -2.0, z = 3.0, w = -4.0)

        val scalarMultiplied = tuple * 3.5

        assertThat(scalarMultiplied).isEqualTo(tuple(x = 3.5, y = -7.0, z = 10.5, w = -14.0))
    }

    @Test
    fun `should multiply a tuple by a fraction`() {
        val tuple = tuple(x = 1.0, y = -2.0, z = 3.0, w = -4.0)

        val scalarMultiplied = tuple * 0.5

        assertThat(scalarMultiplied).isEqualTo(tuple(x = 0.5, y = -1.0, z = 1.5, w = -2.0))
    }

    @Test
    fun `should divide a tuple by a scalar`() {
        val tuple = tuple(x = 1.0, y = -2.0, z = 3.0, w = -4.0)

        val scalarMultiplied = tuple / 2.0

        assertThat(scalarMultiplied).isEqualTo(tuple(x = 0.5, y = -1.0, z = 1.5, w = -2.0))
    }

    @Test
    fun `should compute magnitude`() {
        assertThat(vector(1.0, 0.0, 0.0).magnitude).isEqualTo(1.0)
        assertThat(vector(0.0, 1.0, 0.0).magnitude).isEqualTo(1.0)
        assertThat(vector(0.0, 0.0, 1.0).magnitude).isEqualTo(1.0)
        assertThat(vector(1.0, 2.0, 3.0).magnitude).isEqualTo(sqrt(14.0))
        assertThat(vector(-1.0, -2.0, -3.0).magnitude).isEqualTo(sqrt(14.0))
    }

    fun `should normalize vectors`() {
        assertThat(vector(4.0, 0.0, 0.0).normalized).isEqualTo(vector(1.0, 0.0, 0.0))
        assertThat(vector(1.0, 2.0, 3.0).normalized).isEqualTo(vector(0.26726, 0.53452, 0.80178))
    }

    @Test
    fun `magnitude of a normalized vector should be 1`() {
        val normalized = vector(1.0, 2.0, 3.0).normalized
        val magnitude = normalized.magnitude

        assertThat(magnitude).isEqualTo(1.0)
    }

    @Test
    fun `should calculate dot product`() {
        val a = vector(1.0, 2.0, 3.0)
        val b = vector(2.0, 3.0, 4.0)

        val dotProduct = a dot b

        assertThat(dotProduct).isEqualTo(20.0)
    }

    @Test
    fun `should calculate cross product`() {
        val a = vector(1.0, 2.0, 3.0)
        val b = vector(2.0, 3.0, 4.0)

        assertThat(a cross b).isEqualTo(vector(-1.0, 2.0, -1.0))
        assertThat(b cross a).isEqualTo(vector(1.0, -2.0, 1.0))
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
}
