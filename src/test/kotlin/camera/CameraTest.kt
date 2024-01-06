package camera

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import matrix.Matrix
import matrix.identityMatrix
import org.junit.jupiter.api.Test
import ray.Ray
import ray.ray
import transformation.rotationY
import transformation.translation
import tuple.point
import tuple.vector
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
        val camera = camera(hsize = 201, vsize = 101, fov = Math.PI /2)
        val ray = camera.rayForPixel(x = 100, y = 50)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0,0,0))
            prop(Ray::direction).isEqualTo(vector(0,0,-1))
        }
    }

    @Test
    fun `should construct ray through a corner of the canvas`() {
        val camera = camera(hsize = 201, vsize = 101, fov = Math.PI /2)
        val ray = camera.rayForPixel(x = 0, y = 0)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0,0,0))
            prop(Ray::direction).isEqualTo(vector(0.66519, 0.33259, -0.66851))
        }
    }

    @Test
    fun `should construct ray when camera transformed`() {
        val camera = camera(
            hsize = 201,
            vsize = 101,
            fov = Math.PI / 2,
            transform = rotationY(Math.PI/4) * translation(0.0,-2.0,5.0)
        )
        val ray = camera.rayForPixel(x = 100, y = 50)

        assertThat(ray).all {
            prop(Ray::origin).isEqualTo(point(0,2,-5))
            prop(Ray::direction).isEqualTo(vector(sqrt(2.0)/2, 0.0, -sqrt(2.0)/2))
        }
    }

    private fun Camera.rayForPixel(x: Int, y: Int): Ray {
        val xOffset = (x + 0.5) * pixelSize
        val yOffset = (y + 0.5) * pixelSize

        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset

        val pixel = transform.inversed() * point(worldX, worldY, -1.0)
        val origin = transform.inversed() * point(0.0,0.0,0.0)
        val direction = (pixel - origin).normalized

        return ray(origin = origin, direction = direction)
    }

    class Camera(
        val hsize: Int,
        val vsize: Int,
        val fov: Double,
        val transform: Matrix,
        val halfWidth: Double,
        val halfHeight: Double,
        val pixelSize: Double,
    )

    private fun camera(
        hsize: Int,
        vsize: Int,
        fov: Double,
        transform: Matrix = identityMatrix(),
    ): Camera {
        val halfView = Math.tan(fov / 2.0)
        val aspectRatio = hsize.toDouble() / vsize.toDouble()
        val halfWidth: Double
        val halfHeight: Double

        if(aspectRatio >= 1.0) {
            halfWidth = halfView
            halfHeight = halfView / aspectRatio
        } else {
            halfWidth = halfView * aspectRatio
            halfHeight = halfView
        }

        return Camera(
            hsize = hsize,
            vsize = vsize,
            fov = fov,
            transform = transform,
            halfWidth = halfWidth,
            halfHeight = halfHeight,
            pixelSize = (halfWidth * 2) / hsize,
        )
    }

}