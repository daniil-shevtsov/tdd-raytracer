package camera

import canvas.Canvas
import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.Color
import kotlinx.coroutines.*
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

data class Kek(val x: Int, val y: Int, val color: Color)

@OptIn(ExperimentalTime::class)
fun Camera.render(world: World): Canvas {
    return runBlocking {
        println("Starting render of canvas")
        val (canvas, timeTaken) = measureTimedValue {
            val pixels = ConcurrentHashMap<Pair<Int,Int>, Color>()

            val raytracingTime = measureTime {
                val deferredResults  = (0..hsize).map { x ->
                    (0..vsize).map { y ->
                        async(Dispatchers.Default) {
                            val ray = rayForPixel(x = x, y = y)
                            val color = world.colorAt(ray)
                           Kek(x = x, y = y, color = color)
                        }
                    }
                }.flatten()
                val results = awaitAll(*deferredResults.toTypedArray())
                results.forEach { kek ->
                    pixels[kek.x to kek.y] = kek.color
                }
            }
            println("raytracing for $hsize x $vsize: $raytracingTime")

            val (canvas, canvasTime) = measureTimedValue {
                canvas(width = hsize, height = vsize).applyToEveryPixel { x, y ->
                    pixels[x to y]!!
                }
            }
            println("created canvas for $hsize x $vsize: $canvasTime")
            canvas
        }
        println("CANVAS RENDER  $hsize x $vsize: $timeTaken")
        canvas
    }
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