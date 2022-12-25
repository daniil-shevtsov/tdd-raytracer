package ray

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import tuple.point
import tuple.vector

internal class RayTest {

    @Test
    fun `should create ray`() {
        val origin = point(1.0, 2.0, 3.0)
        val direction = vector(4.0, 5.0, 6.0)

        val ray = ray(origin, direction)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(origin)
            prop(Ray::direction).isEqualTo(direction)
        }
    }

    @Test
    fun `should compute a point from a distance`() {
        val ray = ray(
            origin = point(2.0, 3.0, 4.0),
            direction = vector(1.0, 0.0, 0.0)
        )

        assertThat(ray.position(at = 0.0)).isEqualTo(point(2.0, 3.0, 4.0))
        assertThat(ray.position(at = 1.0)).isEqualTo(point(3.0, 3.0, 4.0))
        assertThat(ray.position(at = -1.0)).isEqualTo(point(1.0, 3.0, 4.0))
        assertThat(ray.position(at = 2.5)).isEqualTo(point(4.5, 3.0, 4.0))
    }

    @Test
    fun `a ray intersects a sphere at two points`() {
        val ray = ray(
            origin = point(0.0, 0.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)

        assertThat(xs).hasTs(t1 = 4.0, t2 = 6.0)
    }

    @Test
    fun `a ray intersects a sphere at tangent`() {
        val ray = ray(
            origin = point(0.0, 1.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)

        assertThat(xs).hasTs(t1 = 5.0, t2 = 5.0)
    }

    @Test
    fun `a ray misses the sphere`() {
        val ray = ray(
            origin = point(0.0, 2.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `a ray originates inside a sphere`() {
        val ray = ray(
            origin = point(0.0, 0.0, 0.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)
        assertThat(xs).hasTs(t1 = -1.0, t2 = 1.0)
    }

    private fun Assert<List<Double>>.hasTs(t1: Double, t2: Double) = all {
        size().isEqualTo(2)
        index(0).isEqualTo(t1)
        index(1).isEqualTo(t2)
    }

    @Test
    fun `a sphere is behind ray`() {
        val ray = ray(
            origin = point(0.0, 0.0, 5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)
        assertThat(xs).all {
            size().isEqualTo(2)
            index(0).isEqualTo(-6.0)
            index(1).isEqualTo(-4.0)
        }
        assertThat(xs).hasTs(t1 = -6.0, t2 = -4.0)
    }

    @Test
    fun `an intersection encapsulates t and object`() {
        val sphere = sphere()
        val intersection = intersection(t = 3.5, intersected = sphere)

        assertThat(intersection).all {
            prop(Intersection::t).isEqualTo(3.5)
            prop(Intersection::intersected).isEqualTo(sphere)
        }
    }

    @Test
    fun `aggregating intersections`() {
        val sphere = sphere()
        val xs = intersections(
            intersection(1.0, sphere),
            intersection(2.0, sphere),
        )

        assertThat(xs).all {
            size().isEqualTo(2)
            index(0).prop(Intersection::t).isEqualTo(1.0)
            index(1).prop(Intersection::t).isEqualTo(2.0)
        }
    }
}