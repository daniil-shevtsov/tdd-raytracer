package fallingsand

import grid.Grid

sealed interface FallingSandAction {
    object Tick : FallingSandAction
    data class CreateSand(val row: Int, val column: Int) : FallingSandAction
    data class CreateAir(val row: Int, val column: Int) : FallingSandAction
    object TogglePause : FallingSandAction
    data class MoveCursor(val direction: Direction) : FallingSandAction
    data class Spawn(val cellType: CellType) : FallingSandAction
}

fun fallingSandSimulation(
    currentState: FallingSandSimulationState,
    action: FallingSandAction,
): FallingSandSimulationState {
    val currentGrid = currentState.grid
    val newState = when (action) {
        is FallingSandAction.CreateAir -> currentState.copy(
            grid = currentGrid.update { row, column, value ->
                if (row == action.row && column == action.column) {
                    value.copy(type = CellType.Air)
                } else {
                    value
                }
            }
        )
        is FallingSandAction.CreateSand -> currentState.copy(
            grid = currentGrid.update { row, column, value ->
                if (row == action.row && column == action.column) {
                    value.copy(type = CellType.Sand)
                } else {
                    value
                }
            }
        )
        FallingSandAction.Tick -> currentState.copy(
            grid = applyNextChangeToGrid(currentGrid)
        )
        is FallingSandAction.MoveCursor -> currentState.copy(
            cursorPosition = currentState.cursorPosition + action.direction.toPositionOffset()
        )
        is FallingSandAction.Spawn -> currentState.copy(
            grid = currentGrid.update { row, column, value ->
                if (row == 0 && column == 0) {
                    value.copy(type = action.cellType)
                } else {
                    value
                }
            }
        )
        FallingSandAction.TogglePause -> currentState
    }

    return newState
}

fun applyNextChangeToGrid(grid: Grid<FallingSandCell>): Grid<FallingSandCell> {
    return applyChangeToGrid(
        grid = grid,
        changeCandidate = createNextChangeCandidate(grid)
    )
}

fun applyChangeToGrid(grid: Grid<FallingSandCell>, changeCandidate: ChangeCandidate): Grid<FallingSandCell> {
    return when (changeCandidate) {
        is ChangeCandidate.Nothing -> grid
        is ChangeCandidate.Change -> grid.update { row, column, value ->
            when (position(row, column)) {
                changeCandidate.sourcePosition -> value.copy(type = CellType.Air)
                changeCandidate.destinationPosition -> value.copy(type = changeCandidate.newType)
                else -> value
            }
        }
    }
}

fun createFallingSandGrid(
    size: Int,
    init: (row: Int, column: Int) -> CellType
): Grid<FallingSandCell> {
    return Grid.createInitialized(size = size) { row, column ->
        fallingSandCell(
            position = position(row = row, column = column),
            type = init(row, column),
        )
    }
}

sealed interface ChangeCandidate {
    data class Change(
        val sourcePosition: Position,
        val destinationPosition: Position,
        val newType: CellType,
    ) : ChangeCandidate

    object Nothing : ChangeCandidate
}