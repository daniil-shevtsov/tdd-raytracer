package ray.practice

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import camera.camera
import camera.render
import canvas.Canvas
import canvas.MyCanvas
import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.Color
import canvas.color.color
import ray.*
import transformation.*
import tuple.Point
import tuple.point
import tuple.vector
import world.world
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposePractice(
    modifier: Modifier = Modifier,
) {
    var lightPosition by remember { mutableStateOf(point(-10, 10, -10)) }
    var rayOrigin by remember { mutableStateOf(point(0.0, 1.5, -5.0)) }
    var color by remember { mutableStateOf(color(1.0, 0.2, 1.0)) }
    val requester = remember { FocusRequester() }

    Column {
        //Text("light=${lightPosition} ray origin=${rayOrigin}")
        MyCanvas(
            canvas = controlledLitSpherePracticeWithCamera(lightPosition, rayOrigin, color),
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
                        Random.nextDouble(0.5, 1.0),
                        Random.nextDouble(0.5, 1.0),
                        Random.nextDouble(0.5, 1.0),
                    )
                    else -> color
                }
                if (isCamera) {
                    rayOrigin = rayOrigin + direction * step
                } else {
                    lightPosition = lightPosition + direction * step * 15.0
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

fun controlledLitSpherePracticeWithCamera(
    lightPosition: Point,
    rayOrigin: Point,
    color: Color,
): Canvas {
    val canvasPixels = 250
    val wallZ = 10.0
    val wallSize = 7.0
    val pixelSize = wallSize / canvasPixels
    val half = wallSize / 2.0
    val sphere = sphere(material = material(color = color))

    val planeScaling = scaling(10.0, 0.01, 10.0)
    val floor = sphere(
        material = material(color = color(1.0, 0.9, 0.9), specular = 0.0),
        transform = planeScaling,
    )
    val leftWall = sphere(
        material = floor.material,
        transform = translation(0.0, 0.0, 5.0) * rotationY(-Math.PI / 4) * rotationX(Math.PI / 2) * planeScaling,
    )
    val rightWall = sphere(
        material = leftWall.material,
        transform = translation(0.0, 0.0, 5.0) * rotationY(Math.PI / 4) * rotationX(Math.PI / 2) * planeScaling,
    )
    val roomMiddleSphere = sphere(
        material = material(
            color = color(0.1, 1.0, 0.5),
            diffuse = 0.7,
            specular = 0.3,
        ),
        transform = translation(-0.5, 1.0, 0.5)
    )
    val roomRightSphere = sphere(
        material = material(
            color = color(0.5, 1.0, 0.1),
            diffuse = 0.7,
            specular = 0.3,
        ),
        transform = translation(1.5, 0.5, -0.5) * scaling(0.5, 0.5, 0.5)
    )
    val roomLeftSphere = sphere(
        material = material(
            color = color,
            diffuse = 0.7,
            specular = 0.3,
        ),
        transform = translation(-1.5, 0.33, -0.75) * scaling(0.33, 0.33, 0.33)
    )

    val light = pointLight(
        position = lightPosition,
        intensity = color(1, 1, 1),
    )
    val camera = camera(
        hsize = canvasPixels,
        vsize = canvasPixels,
        fov = Math.PI / 3,
        transform = viewTransform(
            from = rayOrigin,
            to = point(0.0, 1.0, 0.0),
            up = vector(0.0, 1.0, 0.0)
        )
    )
    val world = world(
        objects = listOf(
            floor,
            leftWall,
            rightWall,
            roomMiddleSphere,
            roomRightSphere,
            roomLeftSphere,
        ),
        lightSources = listOf(light)
    )

    return camera.render(world)
}

