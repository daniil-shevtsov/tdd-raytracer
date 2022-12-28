package ray.practice

import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.color
import canvas.toPmm
import ray.*
import tuple.point

fun litSphereOnCanvas(): String {
    val canvasPixels = 100
    val wallZ = 10.0
    val wallSize = 7.0
    val pixelSize = wallSize / canvasPixels
    val half = wallSize / 2.0
    val sphere = sphere(material = material(color = color(1.0, 0.2, 1.0)))

    val light = pointLight(
        position = point(-10, 10, -10),
        intensity = color(1, 1, 1),
    )

    return canvas(width = canvasPixels, height = canvasPixels).applyToEveryPixel { x, y ->
        val worldY = half - pixelSize * y
        val worldX = -half + pixelSize * x
        val wallColor = color(1.0, 0.0, 0.0)
        val shadowColor = color(0.0, 0.0, 0.0)
        val position = point(worldX, worldY, wallZ)
        val shape = sphere()
        val rayOrigin = point(0.0, 0.0, -5.0)
        val ray = ray(
            origin = rayOrigin,
            direction = (position - rayOrigin).normalized,
        )
        val xs = intersection(shape, ray)

        val hit = hit(xs)


        when (hit) {
            null -> wallColor
            else -> {
                val hitObject = hit.intersected
                val point = ray.position(at = hit.t)
                val normal = hitObject.normalAt(point)
                val eye = -ray.direction
                val color = hitObject.material.litBy(
                    light = light,
                    point = point,
                    eye = eye,
                    normal = normal,
                )
                color
            }
        }
    }.toPmm()
}