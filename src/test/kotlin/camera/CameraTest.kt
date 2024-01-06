package camera

import assertk.all
import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import matrix.Matrix
import matrix.identityMatrix
import org.junit.jupiter.api.Test

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

    class Camera(
        val hsize: Int,
        val vsize: Int,
        val fov: Double,
        val transform: Matrix,
        val halfWidth: Double,
        val halfHeight: Double,
        val pixelSize: Double,
    )

    private fun camera(hsize: Int, vsize: Int, fov: Double): Camera {
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
            transform = identityMatrix(),
            halfWidth = halfWidth,
            halfHeight = halfHeight,
            pixelSize = (halfWidth * 2) / hsize,
        )
    }

}