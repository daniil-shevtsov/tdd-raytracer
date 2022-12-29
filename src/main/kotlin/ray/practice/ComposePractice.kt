package ray.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import canvas.Canvas
import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.Color
import canvas.color.color
import ray.*
import tuple.Point
import tuple.point
import tuple.vector
import java.lang.Float.max
import kotlin.random.Random
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color as ComposeColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposePractice(
    modifier: Modifier = Modifier,
) {
    var lightPosition by remember { mutableStateOf(point(-10, 10, -10)) }
    var rayOrigin by remember { mutableStateOf(point(0.0, 0.0, -5.0)) }
    var color by remember { mutableStateOf(color(1.0, 0.2, 1.0)) }
    val requester = remember { FocusRequester() }
    Column {
        MyCanvas(
            canvas = controlledLitSpherePractice(lightPosition, rayOrigin, color),
            modifier.onKeyEvent {
                val step = 0.1
                val direction = vector(
                    x = when (it.key) {
                        Key.A -> -1.0
                        Key.D -> 1.0
                        else -> 0.0
                    },
                    y = when (it.key) {
                        Key.W -> -1.0
                        Key.S -> 1.0
                        else -> 0.0
                    },
                    z = 0.0,
                )
                val isCamera = it.isShiftPressed
                color = when (it.key) {
                    Key.Spacebar -> color(
                        Random.nextDouble(0.0, 1.0),
                        Random.nextDouble(0.0, 1.0),
                        Random.nextDouble(0.0, 1.0),
                    )
                    else -> color
                }
                if (isCamera) {
                    rayOrigin = rayOrigin + direction * step
                } else {
                    lightPosition = lightPosition + direction * step * 3.0
                }
                direction.components.any { it != 0.0 } || it.key == Key.Spacebar
            }
                .focusRequester(requester)
                .focusable()
        )
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}

@Composable
fun MyCanvas(canvas: Canvas, modifier: Modifier = Modifier) {
    ComposeCanvas(modifier = modifier.fillMaxSize().background(ComposeColor.Gray)) {
        val horizontalPixelCount = size.width
        val verticalPixelCount = size.height
        val pixelSize = max(horizontalPixelCount / canvas.pixels.first().size, verticalPixelCount / canvas.pixels.size)
        canvas.pixels.forEachIndexed { rowIndex, pixelRow ->
            pixelRow.forEachIndexed { columnIndex, pixelColor ->
                val horizontal = columnIndex * horizontalPixelCount / pixelRow.size
                val vertical = verticalPixelCount * rowIndex / canvas.pixels.size

                drawCircle(
                    color = pixelColor.toComposeColor(),
                    radius = pixelSize,
                    center = Offset(horizontal, vertical),
                )
            }
        }
    }
}

fun controlledLitSpherePractice(
    lightPosition: Point = point(-10, 10, -10),
    rayOrigin: Point = point(0.0, 0.0, -5.0),
    color: Color = color(1.0, 0.2, 1.0)
): Canvas {
    val canvasPixels = 100
    val wallZ = 10.0
    val wallSize = 7.0
    val pixelSize = wallSize / canvasPixels
    val half = wallSize / 2.0
    val sphere = sphere(material = material(color = color))

    val light = pointLight(
        position = lightPosition,
        intensity = color(1, 1, 1),
    )
    println("light: $lightPosition ray: $rayOrigin")

    return canvas(width = canvasPixels, height = canvasPixels).applyToEveryPixel { x, y ->
        val worldY = half - pixelSize * y
        val worldX = -half + pixelSize * x
        val wallColor = color(0.2, 0.2, 0.2)
        val position = point(worldX, worldY, wallZ)

        val ray = ray(
            origin = rayOrigin,
            direction = (position - rayOrigin).normalized,
        )
        val xs = intersection(sphere, ray)

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
    }
}

private fun Color.toComposeColor() = ComposeColor(
    red = red.toFloat().coerceAtMost(maximumValue = 1.0f),
    green = green.toFloat().coerceAtMost(maximumValue = 1.0f),
    blue = blue.toFloat().coerceAtMost(maximumValue = 1.0f),
    alpha = 1.0f,
)

