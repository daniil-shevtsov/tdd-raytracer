package transformation

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import tuple.point
import tuple.vector

internal class TransformationKtTest {

    @Test
    fun `multiplying by a translation matrix`() {
        val transform = translation(5.0, -3.0, 2.0)
        val point = point(-3.0, 4.0, 5.0)
        assertThat(transform * point).isEqualTo(point(2.0, 1.0, 7.0))
    }

    @Test
    fun `multiplying by an inverse of a translation matrix`() {
        val inverse = translation(5.0, -3.0, 2.0).inversed()!!
        val point = point(-3.0, 4.0, 5.0)
        assertThat(inverse * point).isEqualTo(point(-8.0, 7.0, 3.0))
    }

    @Test
    fun `translation does not affect vectors`() {
        val transform = translation(5.0, -3.0, 2.0)
        val vector = vector(-3.0, 4.0, 5.0)
        assertThat(transform * vector).isEqualTo(vector)
    }

    @Test
    fun `scaling matrix applied to a point`() {
        val transform = scaling(2.0, 3.0, 4.0)
        val point = point(-4.0, 6.0, 8.0)
        assertThat(transform * point).isEqualTo(point(-8.0, 18.0, 32.0))
    }

    @Test
    fun `scaling matrix applied to a vector`() {
        val transform = scaling(2.0, 3.0, 4.0)
        val vector = vector(-4.0, 6.0, 8.0)
        assertThat(transform * vector).isEqualTo(vector(-8.0, 18.0, 32.0))

    }

    @Test
    fun `multiplying by the inverse of a scaling matrix`() {
        val inversedTransform = scaling(2.0, 3.0, 4.0).inversed()!!
        val vector = vector(-4.0, 6.0, 8.0)

        assertThat(inversedTransform * vector).isEqualTo(vector(-2.0, 2.0, 2.0))
    }

}