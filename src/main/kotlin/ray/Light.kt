package ray

import canvas.color.Color
import tuple.Point

data class Light(
    val position: Point,
    val intensity: Color,
)

data class Material(
    val ambient: Double,
    val diffuse: Double,
    val specular: Double,
    val shininess: Double,
)

fun material(
    ambient: Double = 0.1,
    diffuse: Double = 0.9,
    specular: Double = 0.9,
    shininess: Double = 200.0,
) = Material(
    ambient = ambient,
    diffuse = diffuse,
    specular = specular,
    shininess = shininess,
)

fun pointLight(
    position: Point,
    intensity: Color,
) = Light(
    position = position,
    intensity = intensity,
)