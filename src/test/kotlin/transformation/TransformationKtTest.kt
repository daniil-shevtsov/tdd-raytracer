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

}