package ray

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import transformation.scaling
import transformation.translation
import tuple.point
import tuple.vector
import kotlin.math.sqrt

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

    @Test
    fun `a sphere is behind ray`() {
        val ray = ray(
            origin = point(0.0, 0.0, 5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()
        val xs = intersection(sphere, ray)

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

    @Test
    fun `intersect sets the object on the intersection`() {
        val ray = ray(
            origin = point(0.0, 0.0, -5.0),
            direction = vector(0.0, 0.0, 1.0)
        )
        val sphere = sphere()

        val xs = intersection(sphere, ray)

        assertThat(xs)
            .extracting(Intersection::intersected)
            .containsExactly(
                sphere,
                sphere,
            )
    }

    @Test
    fun `the hit when all intersections have positive t`() {
        val sphere = sphere()
        val i1 = intersection(1.0, sphere)
        val i2 = intersection(2.0, sphere)
        val xs = intersections(i2, i1)

        val hit = hit(xs)

        assertThat(hit).isEqualTo(i1)
    }

    @Test
    fun `the hit when some intersections have negative t`() {
        val sphere = sphere()
        val i1 = intersection(-1.0, sphere)
        val i2 = intersection(1.0, sphere)
        val xs = intersections(i2, i1)

        val hit = hit(xs)

        assertThat(hit).isEqualTo(i2)
    }

    @Test
    fun `the hit when all intersections have negative t`() {
        val sphere = sphere()
        val i1 = intersection(-2.0, sphere)
        val i2 = intersection(-1.0, sphere)
        val xs = intersections(i2, i1)

        val hit = hit(xs)

        assertThat(hit).isNull()
    }

    @Test
    fun `the hit is always the lowest non-negative intersection`() {
        val sphere = sphere()
        val i1 = intersection(5.0, sphere)
        val i2 = intersection(7.0, sphere)
        val i3 = intersection(-3.0, sphere)
        val i4 = intersection(2.0, sphere)
        val xs = intersections(i1, i2, i3, i4)

        val hit = hit(xs)

        assertThat(hit).isEqualTo(i4)
    }

    @Test
    fun `the hit is always the lowest non-negative intersection with other order`() {
        val sphere = sphere()
        val i1 = intersection(5.0, sphere)
        val i2 = intersection(7.0, sphere)
        val i3 = intersection(-3.0, sphere)
        val i4 = intersection(2.0, sphere)
        val xs = intersections(i1, i4, i2, i3)

        val hit = hit(xs)

        assertThat(hit).isEqualTo(i4)
    }

    @Test
    fun `should translate a ray`() {
        val ray = ray(
            origin = point(1.0, 2.0, 3.0),
            direction = vector(0.0, 1.0, 0.0)
        )
        val newRay = ray.transformBy(translation(3.0, 4.0, 5.0))

        assertThat(newRay).all {
            prop(Ray::origin).isEqualTo(point(4.0, 6.0, 8.0))
            prop(Ray::direction).isEqualTo(vector(0.0, 1.0, 0.0))
        }
    }

    @Test
    fun `should scale a ray`() {
        val ray = ray(
            origin = point(1.0, 2.0, 3.0),
            direction = vector(0.0, 1.0, 0.0)
        )
        val newRay = ray.transformBy(scaling(2.0, 3.0, 4.0))

        assertThat(newRay).all {
            prop(Ray::origin).isEqualTo(point(2.0, 6.0, 12.0))
            prop(Ray::direction).isEqualTo(vector(0.0, 3.0, 0.0))
        }
    }

    @Test
    fun `should reflect ray approaching at 45 degrees`() {
        val ray = vector(1.0, -1.0, 0.0)

        val reflected = ray.reflectFrom(normal = vector(0.0, 1.0, 0.0))

        assertThat(reflected).isEqualTo(vector(1.0, 1.0, 0.0))
    }

    @Test
    fun `should reflect from slanted surface`() {
        val ray = vector(0.0, -1.0, 0.0)

        val reflected = ray.reflectFrom(normal = vector(sqrt(2.0) / 2.0, sqrt(2.0) / 2.0, 0.0))

        assertThat(reflected).isEqualTo(vector(1.0, 0.0, 0.0))
    }

}

fun Assert<Intersections>.hasTs(t1: Double, t2: Double) = all {
    size().isEqualTo(2)
    extracting(Intersection::t)
        .all {
            index(0).isEqualTo(t1)
            index(1).isEqualTo(t2)
        }
}