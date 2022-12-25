package ray.practice

import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.color
import canvas.toPmm
import ray.hit
import ray.intersection
import ray.ray
import ray.sphere
import tuple.point

fun sphereOnCanvas(): String {
    val canvasPixels = 100
    val wallZ = 10.0
    val wallSize = 7.0
    val pixelSize = wallSize / canvasPixels
    val half = wallSize / 2.0

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

        when (hit(xs)) {
            null -> wallColor
            else -> shadowColor
        }
    }.toPmm()
}