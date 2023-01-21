package fallingsand

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
import canvas.MyCanvas
import canvas.color.color
import ray.practice.controlledLitSpherePractice
import tuple.point
import tuple.vector
import kotlin.random.Random

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