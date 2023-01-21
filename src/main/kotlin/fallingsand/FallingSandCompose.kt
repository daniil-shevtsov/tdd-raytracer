package fallingsand

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import canvas.MyCanvas
import grid.Grid
import grid.toCanvas

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FallingSandCompose(
    modifier: Modifier = Modifier,
) {
    var currentGrid by remember {
        mutableStateOf(
            Grid.createInitialized(100) { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = when {
                        row == 1 && (column % 2) == 0 -> CellType.Sand
                        else -> CellType.Air
                    }
                )
            }
        )
    }
    val requester = remember { FocusRequester() }
    Column {
        MyCanvas(
            canvas = currentGrid.toCanvas(),
            modifier.onKeyEvent {
                when (it.key) {
                    Key.Spacebar -> {
                        currentGrid = fallingSandSimulation(currentGrid)
                    }
                }
                false
            }
                .focusRequester(requester)
                .focusable()
        )
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}