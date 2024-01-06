package camera

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import canvas.color.color
import canvas.pixelAt
import matrix.identityMatrix
import org.junit.jupiter.api.Test
import ray.Ray
import transformation.rotationY
import transformation.translation
import transformation.viewTransform
import tuple.point
import tuple.vector
import world.defaultWorld
import kotlin.math.sqrt

class CameraTest {

    @Test
    fun `should create a camera`() {
        val camera = camera(hsize = 160, vsize = 120, fov = Math.PI / 2)
        assertThat(camera).all {
            prop(Camera::hsize).isEqualTo(160)
            prop(Camera::vsize).isEqualTo(120)
            prop(Camera::fov).isEqualTo(Math.PI / 2)
            prop(Camera::transform).isEqualTo(identityMatrix())
        }
    }

    @Test
    fun `should calculate pixel size for horizontal canvas`() {
        val camera = camera(hsize = 200, vsize = 125, fov = Math.PI / 2)
        assertThat(camera).prop(Camera::pixelSize).isCloseTo(0.01, delta = 0.00001)
    }

    @Test
    fun `should calculate pixel size for vertical canvas`() {
        val camera = camera(hsize = 125, vsize = 200, fov = Math.PI / 2)
        assertThat(camera).prop(Camera::pixelSize).isCloseTo(0.01, delta = 0.00001)
    }

    @Test
    fun `should construct ray through the center of the canvas`() {
        val camera = camera(hsize = 201, vsize = 101, fov = Math.PI / 2)
        val ray = camera.rayForPixel(x = 100, y = 50)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0, 0, 0))
            prop(Ray::direction).isEqualTo(vector(0, 0, -1))
        }
    }

    @Test
    fun `should construct ray through a corner of the canvas`() {
        val camera = camera(hsize = 201, vsize = 101, fov = Math.PI / 2)
        val ray = camera.rayForPixel(x = 0, y = 0)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0, 0, 0))
            prop(Ray::direction).isEqualTo(vector(0.66519, 0.33259, -0.66851))
        }
    }

    @Test
    fun `should construct ray when camera transformed`() {
        val camera = camera(
            hsize = 201,
            vsize = 101,
            fov = Math.PI / 2,
            transform = rotationY(Math.PI / 4) * translation(0.0, -2.0, 5.0)
        )
        val ray = camera.rayForPixel(x = 100, y = 50)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0, 2, -5))
            prop(Ray::direction).isEqualTo(vector(sqrt(2.0) / 2, 0.0, -sqrt(2.0) / 2))
        }
    }

    @Test
    fun `should render a world with a camera`() {
        val world = defaultWorld()
        val camera = camera(
            hsize = 11,
            vsize = 11,
            fov = Math.PI / 2,
            transform = viewTransform(
                from = point(0, 0, -5),
                to = point(0, 0, 0),
                up = vector(0, 1, 0),
            )
        )

        val canvas = camera.render(world)

        assertThat(canvas.pixelAt(x = 5, y = 5)).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }

}