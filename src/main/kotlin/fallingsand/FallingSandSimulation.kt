package fallingsand

import grid.Grid

sealed interface FallingSandAction {
    object Tick : FallingSandAction
    data class CreateSand(val row: Int, val column: Int) : FallingSandAction
    data class CreateAir(val row: Int, val column: Int) : FallingSandAction
}

fun fallingSandSimulation(
    currentGrid: Grid<FallingSandCell>,
    action: FallingSandAction,
): Grid<FallingSandCell> {
    return when (action) {
        is FallingSandAction.CreateAir -> currentGrid.update { row, column, value ->
            if (row == action.row && column == action.column) {
                value.copy(type = CellType.Air)
            } else {
                value
            }
        }
        is FallingSandAction.CreateSand -> currentGrid.update { row, column, value ->
            if (row == action.row && column == action.column) {
                value.copy(type = CellType.Sand)
            } else {
                value
            }
        }
        FallingSandAction.Tick -> applyNextChangeToGrid(currentGrid)
    }
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

private fun applyChangeCandidate(candidate: ChangeCandidate, cell: FallingSandCell): FallingSandCell {
    return when (candidate) {
        is ChangeCandidate.Change -> cell.copy(
            position = candidate.destinationPosition,
            type = candidate.newType,
        )
        ChangeCandidate.Nothing -> cell
    }
}