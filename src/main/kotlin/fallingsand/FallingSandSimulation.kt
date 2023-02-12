package fallingsand

import grid.Grid
import org.jetbrains.annotations.TestOnly

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

data class ChunkPositions(
    val current: Position,
    val north: Position?,
    val northEast: Position?,
    val east: Position?,
    val southEast: Position?,
    val south: Position?,
    val southWest: Position?,
    val west: Position?,
    val northWest: Position?,
) {
    @TestOnly
    fun asMap() = listOf(
        "current" to current,
        "north" to north,
        "northEast" to northEast,
        "east" to east,
        "southEast" to southEast,
        "south" to south,
        "southWest" to southWest,
        "west" to west,
        "northWest" to northWest,
    ).toMap()
}

data class LogicChunk(
    val south: FallingSandCell?,
    val southEast: FallingSandCell?,
    val southWest: FallingSandCell?,
    val current: FallingSandCell,
)

sealed interface ChangeCandidate {
    data class Change(
        val sourcePosition: Position,
        val destinationPosition: Position,
        val newType: CellType,
    ) : ChangeCandidate

    object Nothing : ChangeCandidate
}

fun createLogicChunk(grid: Grid<FallingSandCell>, cell: FallingSandCell): LogicChunk {
    return LogicChunk(
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
        current = cell,
    )
}

fun updateCell(grid: Grid<FallingSandCell>, cell: FallingSandCell): FallingSandCell {
    val candidate = createChangeCandidate(createLogicChunk(grid, cell), cell)
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

fun createChangeCandidate(logicChunk: LogicChunk, cell: FallingSandCell): ChangeCandidate {
    val currentIsSandRule = { logicChunk: LogicChunk -> logicChunk.current.type == CellType.Sand }
    val airExistsInAnyBelowDirection = { logicChunk: LogicChunk ->
        CellType.Air in listOfNotNull(
            logicChunk.south,
            logicChunk.southEast,
            logicChunk.southWest
        ).map(FallingSandCell::type)
    }
    val moveBelowToAnyFreeCellRule = { logicChunk: LogicChunk ->
        listOfNotNull(logicChunk.south, logicChunk.southEast, logicChunk.southWest).filter { it.type == CellType.Air }
            .map(FallingSandCell::position)
            .firstOrNull()?.let { it - logicChunk.current.position } ?: position(0, 0)
    }

    val change = when {
        currentIsSandRule(logicChunk) && airExistsInAnyBelowDirection(logicChunk) -> moveBelowToAnyFreeCellRule(
            logicChunk
        )
        else -> position(0, 0)
    }

    return when (change) {
        position(0, 0) -> ChangeCandidate.Nothing
        else -> ChangeCandidate.Change(
            sourcePosition = position(-1, -1),
            destinationPosition = cell.position.copy(
                row = cell.position.row + change.row,
                column = cell.position.column + change.column
            ),
            newType = CellType.Sand,
        )
    }
}