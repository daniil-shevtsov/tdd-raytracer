package ray

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import canvas.color.color
import org.junit.jupiter.api.Test
import tuple.point

internal class LightTest {

    @Test
    fun `a point light has position and intensity`() {
        val intensity = color(1.0,1.0,1.0)
        val position = point(0.0,0.0,0.0)

        assertThat(pointLight(position, intensity))
            .all {
                prop(Light::intensity).isEqualTo(intensity)
                prop(Light::position).isEqualTo(position)
            }
    }

    @Test
    fun `should create default material`() {
        val material = material()

        assertThat(material).all {
            prop(Material::ambient).isEqualTo(0.1)
            prop(Material::diffuse).isEqualTo(0.9)
            prop(Material::specular).isEqualTo(0.9)
            prop(Material::shininess).isEqualTo(200.0)
        }
    }

    @Test
    fun `sphere can accept material`() {
        val sphere = sphere(material = material(ambient = 1.0))

        assertThat(sphere.material)
            .prop(Material::ambient)
            .isEqualTo(1.0)
    }

}