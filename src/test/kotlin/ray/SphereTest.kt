package ray

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import matrix.identityMatrix
import org.junit.jupiter.api.Test
import transformation.rotationZ
import transformation.scaling
import transformation.translation
import tuple.point
import tuple.vector
import kotlin.math.sqrt

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

    @Test
    fun `normal on a sphere at point on x axis`() {
        val normal = sphere().normalAt(point(1.0, 0.0, 0.0))
        assertThat(normal).isEqualTo(vector(1.0, 0.0, 0.0))
    }

    @Test
    fun `normal on a sphere at point on y axis`() {
        val normal = sphere().normalAt(point(0.0, 1.0, 0.0))
        assertThat(normal).isEqualTo(vector(0.0, 1.0, 0.0))
    }

    @Test
    fun `normal on a sphere at point on z axis`() {
        val normal = sphere().normalAt(point(0.0, 0.0, 1.0))
        assertThat(normal).isEqualTo(vector(0.0, 0.0, 1.0))
    }

    @Test
    fun `normal on a sphere at nonaxial point`() {
        val fraction = sqrt(3.0) / 3.0
        val normal = sphere().normalAt(point(fraction, fraction, fraction))
        assertThat(normal).isEqualTo(vector(fraction, fraction, fraction))
        assertThat(normal).isEqualTo(vector(fraction, fraction, fraction))
    }

    @Test
    fun `normal is normalized vector`() {
        val fraction = sqrt(3.0) / 3.0
        val normal = sphere().normalAt(point(fraction, fraction, fraction))
        assertThat(normal).isEqualTo(normal.normalized)
    }

    @Test
    fun `normal of a translated sphere`() {
        val sphere = sphere()
        val transformed = sphere.transformBy(translation(0.0, 1.0, 0.0))
        assertThat(transformed.normalAt(point(0.0, 1.70711, -0.70711)))
            .isEqualTo(vector(0.0, 0.70711, -0.70711))
    }

    @Test
    fun `normal of a transformed sphere`() {
        val sphere = sphere()
        val transformed = sphere.transformBy(scaling(1.0, 0.5, 1.0) * rotationZ(Math.PI / 5))
        assertThat(transformed.normalAt(point(0.0, sqrt(2.0)/2, -sqrt(2.0)/2)))
            .isEqualTo(vector(0.0, 0.97014, -0.24254))
    }

    @Test
    fun `sphere has a default material`() {
        val sphere = sphere()
        assertThat(sphere.material)
            .isEqualTo(material())
    }
}