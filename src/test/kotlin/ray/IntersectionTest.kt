package ray

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import org.junit.jupiter.api.Test
import tuple.point
import tuple.vector

internal class IntersectionTest {

    @Test
    fun `should precompute state of an intersection`() {
        val ray = ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = sphere()
        val intersection = intersection(4.0, shape)
        val state = intersection.prepareState(ray)

        assertThat(state).all {
            prop(IntersectionState::t).isEqualTo(4.0)
            prop(IntersectionState::intersected).isEqualTo(shape)
            prop(IntersectionState::point).isEqualTo(point(0, 0, -1))
            prop(IntersectionState::eye).isEqualTo(vector(0, 0, -1))
            prop(IntersectionState::normal).isEqualTo(vector(0, 0, -1))
        }
    }

    @Test
    fun `inside should be false when hit is from outside`() {
        val ray = ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = sphere()
        val intersection = intersection(4.0, shape)
        val state = intersection.prepareState(ray)

        assertThat(state).prop(IntersectionState::inside).isFalse()
    }

    @Test
    fun `inside should be true when hit is from inside`() {
        val ray = ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = sphere()
        val intersection = intersection(1.0, shape)
        val state = intersection.prepareState(ray)

        assertThat(state).all {
            prop(IntersectionState::point).isEqualTo(point(0, 0, 1))
            prop(IntersectionState::eye).isEqualTo(vector(0, 0, -1))
            prop(IntersectionState::normal).isEqualTo(vector(0, 0, -1))
            prop(IntersectionState::inside).isTrue()
        }
    }

}