package fallingsand

import grid.Grid
import org.jetbrains.annotations.TestOnly

data class FallingSandSimulationState(
    val grid: Grid<FallingSandCell>,
    val cursorPosition: Position,
    val isPaused: Boolean,
)

@TestOnly
fun fallingSandSimulationState(
    grid: Grid<FallingSandCell> = defaultSandGrid(),
    cursorPosition: Position = position(),
    isPaused: Boolean = false,
) = FallingSandSimulationState(
    grid = grid,
    cursorPosition = cursorPosition,
    isPaused = isPaused,
)