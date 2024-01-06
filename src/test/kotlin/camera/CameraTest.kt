package camera

import assertk.all
import assertk.assertThat
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
            prop(Camera::fov).isEqualTo(Math.PI/2)
            prop(Camera::transform).isEqualTo(identityMatrix())
        }
    }

    class Camera(
        val hsize: Int,
        val vsize: Int,
        val fov: Double,
        val transform: Matrix,
    )

    private fun camera(hsize: Int, vsize: Int, fov: Double): Camera {
        return Camera(
            hsize = hsize,
            vsize = vsize,
            fov = fov,
            transform = identityMatrix(),
        )
    }

}