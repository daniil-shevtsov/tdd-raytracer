package canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import canvas.color.Color
import java.lang.Float
import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun MyCanvas(canvas: Canvas, modifier: Modifier = Modifier) {
    ComposeCanvas(modifier = modifier.fillMaxSize().background(ComposeColor.Gray)) {
        val horizontalPixelCount = size.width
        val verticalPixelCount = size.height
        val pixelSize =
            Float.max(horizontalPixelCount / canvas.pixels.first().size, verticalPixelCount / canvas.pixels.size)
        canvas.pixels.forEachIndexed { rowIndex, pixelRow ->
            pixelRow.forEachIndexed { columnIndex, pixelColor ->
                val horizontal = columnIndex * horizontalPixelCount / pixelRow.size
                val vertical = verticalPixelCount * rowIndex / canvas.pixels.size

                drawRect(
                    color = pixelColor.toComposeColor(),
                    size = Size(pixelSize, pixelSize),
                    topLeft = Offset(horizontal, vertical),
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