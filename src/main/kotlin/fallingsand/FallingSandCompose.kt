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
import grid.toCanvas
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FallingSandCompose(
    modifier: Modifier = Modifier,
) {
    var currentState by remember {
        mutableStateOf(
            FallingSandSimulationState(
                grid = filledSandGrid(size = 10, cellType = CellType.Air),
                cursorPosition = position(0, 0),
                isPaused = false,
            )
        )
    }

    var cursorPosition by remember {
        mutableStateOf(
            position(row = 0, column = 0)
        )
    }
    var isPaused by remember {
        mutableStateOf(true)
    }
    val requester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        launch {
            while (true) {
                delay(10L)
                if (!isPaused) {
                    println("Real time tick")
                    currentState = fallingSandSimulation(currentState, FallingSandAction.Tick)
                }
            }
        }
    }

    Column {
        MyCanvas(
            canvas = currentState.grid.toCanvas(), modifier
//                .onClick(
//                    matcher = PointerMatcher.mouse(PointerButton.Primary),
//                    onClick = {
//                        val row = 0
//                        val column = 0
//                        currentGrid = fallingSandSimulation(currentGrid, FallingSandAction.CreateSand(row = row, column = column))
//                    }
//                )
                .onKeyEvent {
                    val isDirectionKey = it.key.keyCode in (0x25..0x28)
                    if (it.type == KeyEventType.KeyDown && (isDirectionKey || it.key == Key.P/* || it.key == Key.Spacebar*/)) {
                        return@onKeyEvent false
                    }
                    if (it.key == Key.P) {
                        println("Toggle pause")
                        isPaused = !isPaused
                    }
                    val positionX = when {
                        it.key == Key.DirectionLeft && cursorPosition.column > 0 -> -1
                        it.key == Key.DirectionRight && cursorPosition.column < currentState.grid.width - 1 -> 1
                        else -> 0
                    }
                    val positionY = when {
                        it.key == Key.DirectionUp && cursorPosition.row > 0 -> -1
                        it.key == Key.DirectionDown && cursorPosition.row < currentState.grid.height - 1 -> 1
                        else -> 0
                    }
                    if (positionX != 0 || positionY != 0) {
                        cursorPosition = cursorPosition.copy(
                            row = cursorPosition.row + 1 * positionY, column = cursorPosition.column + 1 * positionX
                        )
                        currentState = fallingSandSimulation(
                            currentState, FallingSandAction.MoveCursor(
                                direction = direction(
                                    horizontal = when (it.key) {
                                        Key.DirectionLeft -> HorizontalDirection.West
                                        Key.DirectionRight -> HorizontalDirection.East
                                        else -> HorizontalDirection.Center
                                    }, vertical = when (it.key) {
                                        Key.DirectionUp -> VerticalDirection.North
                                        Key.DirectionDown -> VerticalDirection.South
                                        else -> VerticalDirection.Center
                                    }
                                )
                            )
                        )
                        println("new position: $cursorPosition")
                    }

                    when (it.key) {
                        Key.Spacebar -> {
                            println("One tick")
                            currentState = fallingSandSimulation(currentState, FallingSandAction.Tick)
                        }
                        Key.S -> {
                            currentState = fallingSandSimulation(
                                currentState,
                                FallingSandAction.Spawn(cellType = CellType.Sand)
                            )
                        }
                        Key.A -> {
                            currentState = fallingSandSimulation(
                                currentState,
                                FallingSandAction.Spawn(cellType = CellType.Air)
                            )
                        }
                    }
                    true
                }.focusRequester(requester).focusable()
        )
    }
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }
}