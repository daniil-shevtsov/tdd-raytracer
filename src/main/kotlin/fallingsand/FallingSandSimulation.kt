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

fun updateEveryCell(grid: Grid<FallingSandCell>): Grid<FallingSandCell> {
    val candidates = mutableMapOf<Position, List<FallingSandCell>>()
    val updatedGrid = grid.update { row, column, value ->
        createChangeCandidate(grid, value)
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

data class LogicChunk(
    val south: FallingSandCell?,
    val southEast: FallingSandCell?,
    val southWest: FallingSandCell?,
)

sealed interface ChangeCandidate {
    data class Change(
        val sourcePosition: Position,
        val destinationPosition: Position,
        val newType: CellType,
    ) : ChangeCandidate

    object Nothing : ChangeCandidate
}


fun createChangeCandidate(grid: Grid<FallingSandCell>, cell: FallingSandCell): FallingSandCell {
    val logicChunk = LogicChunk(
        south = when (cell.position.row) {
            grid.height - 1 -> null
            else -> grid[cell.position.row + 1, cell.position.column]
        },
        southEast = when {
            cell.position.row == grid.height - 1 || cell.position.column == grid.width - 1 -> null
            else -> grid[cell.position.row + 1, cell.position.column + 1]
        },
        southWest = when {
            cell.position.row == grid.height - 1 || cell.position.column == 0 -> null
            else -> grid[cell.position.row + 1, cell.position.column - 1]
        },
    )

    return when (val changeCandidate = createChangeCandidate(logicChunk, cell)) {
        is ChangeCandidate.Change -> cell.copy(
            position = changeCandidate.destinationPosition,
            type = changeCandidate.newType,
        )
        ChangeCandidate.Nothing -> cell
    }
}

fun createChangeCandidate(logicChunk: LogicChunk, cell: FallingSandCell): ChangeCandidate {
    val positionChange = when {
        cell.type == CellType.Sand && logicChunk.south?.type == null -> position(row = 0, column = 0)
        cell.type == CellType.Sand && logicChunk.south?.type == CellType.Sand
                && logicChunk.southEast?.type == CellType.Air -> position(row = 1, column = 1)
        cell.type == CellType.Sand && logicChunk.south?.type == CellType.Sand
                && (logicChunk.southEast?.type == CellType.Sand || logicChunk.southEast?.type == null)
                && logicChunk.southWest?.type == CellType.Air -> position(row = 1, column = -1)
        cell.type == CellType.Sand && logicChunk.south?.type == CellType.Air -> position(row = 1, column = 0)
        else -> position(row = 0, column = 0)
    }
    return when {
        positionChange == position(0, 0) -> ChangeCandidate.Nothing
        else -> ChangeCandidate.Change(
            sourcePosition = position(-1, -1),
            destinationPosition = cell.position.copy(
                row = cell.position.row + positionChange.row,
                column = cell.position.column + positionChange.column
            ),
            newType = CellType.Sand,
        )
    }
}