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
    fun `should create default world with given light source`() {
        val light = pointLight(
            position = point(-50, 100, -30),
            intensity = color(0.0, 1.0, 0.5)
        )

        val world = defaultWorldWithLightSource(lightSource = light)

        assertThat(world).prop(World::light).isEqualTo(light)
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

    @Test
    fun `should shade an intersection`() {
        val world = defaultWorld()
        val ray = ray(point(0, 0, -5), vector(0,0,1))
        val shape = world.objects.first()
        val intersection = intersection(4.0, shape)
        val intersectionState = intersection.prepareState(ray)

        val shadeHit = world.shadeHit(intersectionState)

        assertThat(shadeHit).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `should shade an intersection from inside`() {
        val world = defaultWorldWithLightSource(
            lightSource = pointLight(point(0.0, 0.25, 0.0), color(1,1,1))
        )
        val ray = ray(point(0, 0, 0), vector(0,0,1))
        val shape = world.objects[1]
        val intersection = intersection(0.5, shape)
        val intersectionState = intersection.prepareState(ray)

        val shadeHit = world.shadeHit(intersectionState)

        assertThat(shadeHit).isEqualTo(color(0.90498, 0.90498, 0.90498))
    }

    @Test
    fun `color should be pitch black when ray misses`() {
        val world = defaultWorld()
        val ray = ray(point(0,0,-5), vector(0,1,0))

        val color = world.colorAt(ray)

        assertThat(color).isEqualTo(color(0,0,0))
    }

    @Test
    fun `color should be the material color when ray hits`() {
        val world = defaultWorld()
        val ray = ray(point(0,0,-5), vector(0,0,1))

        val color = world.colorAt(ray)

        assertThat(color).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `color should be of the material of the inner sphere`() {
        val world = defaultWorldWithMaxAmbient()
        val innerSphere = world.objects[1]
        val ray = ray(point(0.0,0.0,0.75), vector(0,0,-1))
        val color = world.colorAt(ray)
        assertThat(color).isEqualTo(innerSphere.material.color)
    }
}