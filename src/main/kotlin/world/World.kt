package world

import canvas.color.color
import ray.*
import transformation.scaling
import tuple.point

class World(
    val objects: List<Intersectable>,
    val lightSources: List<Light>,
) {
    val light: Light
        get() = pointLight(
            position = point(-10, 10, -10),
            intensity = color(1, 1, 1),
        )
}

fun world(
    objects: List<Intersectable> = emptyList(),
    lightSources: List<Light> = emptyList(),
) = World(
    objects = objects,
    lightSources = lightSources,
)

fun defaultWorld() = world(
    objects = listOf(
        sphere(
            material = material(
                color = color(0.8, 1.0, 0.6),
                diffuse = 0.7,
                specular = 0.2
            )
        ),
        sphere().transformBy(scaling(0.5, 0.5, 0.5))
    ),
    lightSources = emptyList(),
)