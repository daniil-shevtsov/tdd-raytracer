package ray

import canvas.color.Color
import canvas.color.color
import tuple.Point
import tuple.Vector
import tuple.point
import kotlin.math.pow

data class Light(
    val position: Point,
    val intensity: Color,
)

data class Material(
    val ambient: Double,
    val diffuse: Double,
    val specular: Double,
    val shininess: Double,
    val color: Color,
) {
    fun litBy(
        light: Light,
        eye: Vector,
        normal: Vector,
        point: Point = point(0,0,0),
    ): Color {
        val lightVector = (light.position - point).normalized
        val angleBetweenLightAndNormal = normal.normalized.angleBetween(lightVector).toDegrees()
        val angleBetweenLightAndEye = eye.normalized.angleBetween(lightVector).toDegrees()

        val effectiveColor = color * light.intensity
        val ambient = effectiveColor * ambient

        val lightNormalCosAngle = lightVector dot normal
        val lightBehindSurface = lightNormalCosAngle < 0.0

        val diffuse: Color
        val specular: Color

        if (lightBehindSurface) {
            diffuse = color(0.0, 0.0, 0.0)
            specular = color(0.0, 0.0, 0.0)
        } else {
            diffuse = effectiveColor * this.diffuse * lightNormalCosAngle
            val lightReflectVector = (-lightVector).reflectFrom(normal)
            val reflectEyeCosAngle = lightReflectVector dot eye
            val isLightReflectedAwayFromEye = reflectEyeCosAngle <= 0.0

            specular = if (isLightReflectedAwayFromEye) {
                color(0.0, 0.0, 0.0)
            } else {
                val factor = reflectEyeCosAngle.pow(shininess)

                light.intensity * this.specular * factor
            }
        }

        val finalColor = ambient + diffuse + specular

        return finalColor

        return when {
            angleBetweenLightAndEye == degrees(0.0) -> color(1.9, 1.9, 1.9)
            angleBetweenLightAndEye == degrees(90.0) -> color(1.6364, 1.6364, 1.6364)
            angleBetweenLightAndEye == degrees(45.0) && angleBetweenLightAndNormal == degrees(0.0) -> color(
                1.0,
                1.0,
                1.0
            )
            angleBetweenLightAndEye == degrees(45.0) -> color(0.7364, 0.7364, 0.7364)
            angleBetweenLightAndNormal == angleBetweenLightAndEye
                    && (eye - normal).magnitude < (eye - lightVector).magnitude -> color(0.1, 0.1, 0.1)
            else -> color(0.0, 0.0, 0.0)
        }
    }
}

fun material(
    color: Color = color(1, 1, 1),
    ambient: Double = 0.1,
    diffuse: Double = 0.9,
    specular: Double = 0.9,
    shininess: Double = 200.0,
) = Material(
    ambient = ambient,
    diffuse = diffuse,
    specular = specular,
    shininess = shininess,
    color = color,
)

fun pointLight(
    position: Point,
    intensity: Color,
) = Light(
    position = position,
    intensity = intensity,
)