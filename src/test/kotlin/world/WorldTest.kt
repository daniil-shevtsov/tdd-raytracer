package world

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import canvas.color.color
import org.junit.jupiter.api.Test
import ray.*
import transformation.scaling
import tuple.point
import tuple.vector

internal class WorldTest {

    @Test
    fun `should create empty world`() {
        val world = world()
        assertThat(world).all {
            prop(World::objects).isEmpty()
            prop(World::lightSources).isEmpty()
        }
    }

    @Test
    fun `should create default world`() {
        val sphere1 = sphere(
            material = material(
                color = color(0.8, 1.0, 0.6),
                diffuse = 0.7,
                specular = 0.2
            )
        )
        val sphere2 = sphere().transformBy(scaling(0.5, 0.5, 0.5))

        val light = pointLight(
            position = point(-10, 10, -10),
            intensity = color(1, 1, 1)
        )

        val world = defaultWorld()

        assertThat(world).all {
            prop(World::objects).containsExactly(
                sphere1,
                sphere2,
            )
            prop(World::light).isEqualTo(light)
        }
    }

    @Test
    fun `should intersect world with a ray`() {
        val world = defaultWorld()
        val ray = ray(point(0, 0, -5), vector(0, 0, 1))

        val intersections = ray.intersect(world)

        assertThat(intersections)
            .extracting(Intersection::t)
            .containsExactly(
                4.0,
                4.5,
                5.5,
                6.0,
            )
    }
}