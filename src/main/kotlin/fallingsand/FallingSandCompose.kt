package fallingsand

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
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
            Grid.createInitialized(3) { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = when {
                        //row == 1 && (column % 2) == 0 -> CellType.Sand
                        else -> CellType.Air
                    }
                )
            }
        )
    }
    var cursorPosition by remember {
        mutableStateOf(
            position(row = 0, column = 0)
        )
    }
    val requester = remember { FocusRequester() }
    Column {
        MyCanvas(
            canvas = currentGrid.toCanvas(),
            modifier
//                .onClick(
//                    matcher = PointerMatcher.mouse(PointerButton.Primary),
//                    onClick = {
//                        val row = 0
//                        val column = 0
//                        currentGrid = fallingSandSimulation(currentGrid, FallingSandAction.CreateSand(row = row, column = column))
//                    }
//                )
                .onKeyEvent {
                    if(it.type == KeyEventType.KeyDown) {
                        return@onKeyEvent false
                    }
                    val positionX = when {
                        it.key == Key.DirectionLeft && cursorPosition.column > 0 -> -1
                        it.key == Key.DirectionRight && cursorPosition.column < currentGrid.width - 1 -> 1
                        else -> 0
                    }
                    val positionY = when {
                        it.key == Key.DirectionUp && cursorPosition.row > 0 -> -1
                        it.key == Key.DirectionDown && cursorPosition.row < currentGrid.height - 1 -> 1
                        else -> 0
                    }
                    if (positionX != 0 || positionY != 0) {
                        cursorPosition = cursorPosition.copy(
                            row = cursorPosition.row + 1 * positionY,
                            column = cursorPosition.column + 1 * positionX
                        )
                    }
                    when (it.key) {
                        Key.Spacebar -> {
                            currentGrid = fallingSandSimulation(currentGrid, FallingSandAction.Tick)
                        }
                        Key.S -> {
                            currentGrid = fallingSandSimulation(currentGrid, FallingSandAction.CreateSand(row = cursorPosition.row, column = cursorPosition.column))
                        }
                        Key.A -> {
                            currentGrid = fallingSandSimulation(currentGrid, FallingSandAction.CreateAir(row = cursorPosition.row, column = cursorPosition.column))
                        }
                    }
                    true
                }
                .focusRequester(requester)
                .focusable()
        )
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}