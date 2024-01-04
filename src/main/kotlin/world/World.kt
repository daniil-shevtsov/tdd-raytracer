package world

import canvas.color.Color
import canvas.color.color
import ray.*
import transformation.scaling
import tuple.point
import java.util.Collections.emptyList

data class World(
    val objects: List<Intersectable>,
    val lightSources: List<Light>,
) {
    val light: Light
        get() = lightSources.first()
}

fun world(
    objects: List<Intersectable> = emptyList(),
    lightSources: List<Light> = emptyList(),
) = World(
    objects = objects,
    lightSources = lightSources,
)

fun defaultWorld(overrideAmbient: Double? = null) = world(
    objects = listOf(
        sphere(
            material = material(
                color = color(0.8, 1.0, 0.6),
                diffuse = 0.7,
                specular = 0.2,
            ).let {material ->
                when {
                    overrideAmbient != null -> material.copy(ambient = overrideAmbient)
                    else -> material
                }
            }
        ),
        sphere(material = material().let {material ->
            when {
                overrideAmbient != null -> material.copy(ambient = overrideAmbient)
                else -> material
            }
        }).transformBy(scaling(0.5, 0.5, 0.5))
    ),
    lightSources = listOf(
        pointLight(
            position = point(-10, 10, -10),
            intensity = color(1, 1, 1),
        )
    )
)

fun defaultWorldWithMaxAmbient() = defaultWorld(overrideAmbient = 1.0)

fun defaultWorldWithLightSource(
    lightSource: Light
): World = world(
    objects = defaultWorld().objects,
    lightSources = listOf(lightSource),
)

fun Ray.intersect(world: World): Intersections {
    return world.objects.flatMap { intersectable ->
        intersect(intersectable)
    }.sortedBy { it.t }
}

fun World.shadeHit(intersectionState: IntersectionState): Color {
    return intersectionState.intersected.material.litBy(
        light = light,
        eye = intersectionState.eye,
        normal = intersectionState.normal,
        point = intersectionState.point,
    )
}

fun World.colorAt(ray: Ray): Color {
    val intersections = ray.intersect(world = this)
        .filter {it.t > 0} //TODO: Not sure if I should do this, but right now to pass the test I need this
    return when {
        intersections.isEmpty() -> color(0,0,0)
        else -> {
            val intersection = intersections.first()
            val intersectionState = intersection.prepareState(ray)

            val shadeHit = shadeHit(intersectionState)
            shadeHit
        }
    }

}