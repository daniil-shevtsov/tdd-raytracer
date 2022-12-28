package ray

import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import canvas.color.color
import org.junit.jupiter.api.Test
import tuple.point
import tuple.vector
import kotlin.test.Ignore

internal class LightTest {

    @Test
    fun `a point light has position and intensity`() {
        val intensity = color(1.0, 1.0, 1.0)
        val position = point(0.0, 0.0, 0.0)

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
            prop(Material::color).isEqualTo(color(1,1,1))
        }
    }

    @Test
    fun `sphere can accept material`() {
        val sphere = sphere(material = material(ambient = 1.0))

        assertThat(sphere.material)
            .prop(Material::ambient)
            .isEqualTo(1.0)
    }

    @Test
    fun `should be at full strength when light is behind eye`() {
        val eye = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(
            position = point(0, 0, -10),
            intensity = color(1, 1, 1)
        )

        assertThat(material().litBy(light, eye, normal))
            .isEqualTo(color(1.9, 1.9, 1.9))
    }

    @Test
    fun `should be without specular when eye and light is 45 degrees`() {
        val eye = vector(degrees(0.0), degrees(45.0), degrees(-45.0))
        val normal = vector(0, 0, -1)
        val light = pointLight(
            position = point(0, 0, -10),
            intensity = color(1, 1, 1)
        )

        assertThat(material().litBy(light, eye, normal))
            .isEqualTo(color(1.0, 1.0, 1.0))
    }

    @Test
    fun `should something when light is  offset by 45 degrees`() {
        val eye = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(
            position = point(0, 10, -10),
            intensity = color(1, 1, 1)
        )

        assertThat(material().litBy(light, eye, normal))
            .isEqualTo(color(0.7364, 0.7364, 0.7364))
    }

    @Test
    @Ignore
    fun `should something when eye in the path of reflection vector`() {
        val eye = vector(degrees(0.0), degrees(-45.0), degrees(-45.0))
        val normal = vector(0, 0, -1)
        val light = pointLight(
            position = point(0, 10, -10),
            intensity = color(1, 1, 1)
        )

        assertThat(material().litBy(light, eye, normal))
            .isEqualTo(color(1.6364, 1.6364, 1.6364))
    }

    @Test
    fun `should have only ambient when the light is behind surface`() {
        val eye = vector(0, 0, -1)
        val normal = vector(0, 0, -1)
        val light = pointLight(
            position = point(0, 0, 10),
            intensity = color(1, 1, 1)
        )

        assertThat(material().litBy(light, eye, normal))
            .isEqualTo(color(0.1, 0.1, 0.1))
    }

}