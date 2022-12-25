package ray

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import matrix.identityMatrix
import org.junit.jupiter.api.Test
import transformation.scaling
import transformation.translation
import tuple.point
import tuple.vector

internal class SphereTest {

    @Test
    fun `sphere has default transformation - identity matrix`() {
        val sphere = sphere()

        assertThat(sphere.transform).isEqualTo(identityMatrix())
    }

    @Test
    fun `should return sphere with new transformation`() {
        val transform = translation(2.0, 3.0, 4.0)
        val transformed = sphere().transformBy(transform)

        assertThat(transformed)
            .prop(Sphere::transform)
            .isEqualTo(transform)
    }

    @Test
    fun `intersecting scaled sphere with a ray`() {
        val ray = ray(
            origin = point(0.0, 0.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val transformed = sphere().transformBy(scaling(2.0, 2.0, 2.0))

        val xs = intersection(transformed, ray)

        assertThat(xs).hasTs(t1 = 3.0, t2 = 7.0)
    }

    @Test
    fun `intersecting translated sphere with a ray`() {
        val ray = ray(
            origin = point(0.0, 0.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val transformed = sphere().transformBy(translation(5.0, 0.0, 0.0))

        val xs = intersection(transformed, ray)

        assertThat(xs).isEmpty()
    }

}