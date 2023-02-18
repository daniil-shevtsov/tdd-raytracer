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
        FallingSandAction.Tick -> updateEveryCell(currentGrid)
    }
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

fun updateEveryCell(grid: Grid<FallingSandCell>): Grid<FallingSandCell> {
    val candidates = mutableMapOf<Position, List<FallingSandCell>>()
    val updatedGrid = grid.update { row, column, value ->
        updateCell(grid, value)
    }.getAsLists()
        .flatten()
        .let { originalList ->
            val map = mutableMapOf<Position, List<FallingSandCell>>()
            originalList.forEach { cell ->
                map[cell.position] = map[cell.position].orEmpty() + cell
                candidates[cell.position] = candidates[cell.position].orEmpty() + cell
            }
            map.toMap()
        }
    val firstNonAirCandidate =
        candidates.entries.firstOrNull { it.value.any { it.type != CellType.Air } }?.let {
            it.key to it.value.first { it.type != CellType.Air }
        }

    return grid.update { row, column, value ->
        val currentPosition = position(row, column)
        val newCell = when {
            currentPosition == firstNonAirCandidate?.first -> firstNonAirCandidate?.second
            else -> value
        }
        val oldCell = updatedGrid[currentPosition].orEmpty().firstOrNull { it.type != CellType.Air }
            ?: updatedGrid[currentPosition]?.first()
        val cell = oldCell
        when (cell) {
            null -> value.copy(type = CellType.Air)
            else -> cell
        }
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

fun updateCell(grid: Grid<FallingSandCell>, cell: FallingSandCell): FallingSandCell {
    val candidate = createChangeCandidate(
        grid,
        createChunkPositions(current = cell.position, width = grid.width, height = grid.height)
    )
    return applyChangeCandidate(
        candidate = candidate,
        cell = cell,
    )
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