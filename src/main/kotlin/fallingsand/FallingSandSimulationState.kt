package fallingsand

import grid.Grid
import org.jetbrains.annotations.TestOnly

data class FallingSandSimulationState(
    val grid: Grid<FallingSandCell>
)

@TestOnly
fun fallingSandSimulationState(
    grid: Grid<FallingSandCell> = defaultSandGrid(),
) = FallingSandSimulationState(
    grid = grid,
)