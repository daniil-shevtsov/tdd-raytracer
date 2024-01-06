package camera

import canvas.Canvas
import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.Color
import matrix.Matrix
import matrix.identityMatrix
import ray.Ray
import ray.ray
import tuple.point
import world.World
import world.colorAt
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun Camera.render(world: World): Canvas {
    println("Starting render of canvas")
    val (canvas, timeTaken) = measureTimedValue {
        val pixels = ConcurrentHashMap<Pair<Int,Int>, Color>()
        val raytracingTime = measureTime {
            (0..hsize).forEach { x ->
                (0..vsize).forEach { y ->
                    val ray = rayForPixel(x = x, y = y)
                    val color = world.colorAt(ray)
                    pixels[x to y] = color
                }
            }
        }
        println("raytracing for $hsize x $vsize: $raytracingTime")

        val canvasTime = measureTime {
            canvas(width = hsize, height = vsize).applyToEveryPixel { x, y ->
                pixels[x to y]!!
            }
        }
        println("created canvas for $hsize x $vsize: $canvasTime")
    }
    println("CANVAS RENDER  $hsize x $vsize: $timeTaken")
    return canvas
}

fun Camera.rayForPixel(x: Int, y: Int): Ray {
    val xOffset = (x + 0.5) * pixelSize
    val yOffset = (y + 0.5) * pixelSize

    val worldX = halfWidth - xOffset
    val worldY = halfHeight - yOffset

    val pixel = transform.inversed() * point(worldX, worldY, -1.0)
    val origin = transform.inversed() * point(0.0, 0.0, 0.0)
    val direction = (pixel - origin).normalized

    return ray(origin = origin, direction = direction)
}

class Camera(
    val hsize: Int,
    val vsize: Int,
    val fov: Double,
    val transform: Matrix,
    val halfWidth: Double,
    val halfHeight: Double,
    val pixelSize: Double,
)

fun camera(
    hsize: Int,
    vsize: Int,
    fov: Double,
    transform: Matrix = identityMatrix(),
): Camera {
    val halfView = Math.tan(fov / 2.0)
    val aspectRatio = hsize.toDouble() / vsize.toDouble()
    val halfWidth: Double
    val halfHeight: Double

    if (aspectRatio >= 1.0) {
        halfWidth = halfView
        halfHeight = halfView / aspectRatio
    } else {
        halfWidth = halfView * aspectRatio
        halfHeight = halfView
    }

    return Camera(
        hsize = hsize,
        vsize = vsize,
        fov = fov,
        transform = transform,
        halfWidth = halfWidth,
        halfHeight = halfHeight,
        pixelSize = (halfWidth * 2) / hsize,
    )
}