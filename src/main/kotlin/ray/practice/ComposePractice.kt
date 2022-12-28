package ray.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import canvas.Canvas
import canvas.color.Color
import java.lang.Float.max
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun ComposePractice(
    modifier: Modifier = Modifier,
) {

}

@Composable
fun MyCanvas(canvas: Canvas, modifier: Modifier = Modifier) {
    ComposeCanvas(modifier = Modifier.fillMaxSize().background(ComposeColor.Gray)) {
        val horizontalPixelCount = size.width
        val verticalPixelCount = size.height
        val pixelSize = max(horizontalPixelCount / canvas.pixels.first().size, verticalPixelCount / canvas.pixels.size)
        canvas.pixels.forEachIndexed { rowIndex, pixelRow ->
            pixelRow.forEachIndexed { columnIndex, pixelColor ->
                val horizontal = columnIndex * horizontalPixelCount / pixelRow.size
                val vertical = verticalPixelCount * rowIndex / canvas.pixels.size
                println("draw at $horizontal $vertical")
                drawCircle(
                    color = pixelColor.toComposeColor(),
                    radius = pixelSize,
                    center = Offset(horizontal, vertical),
                )
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

