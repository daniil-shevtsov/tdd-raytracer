package fallingsand

import grid.Grid

fun createChangeCandidate(
    grid: Grid<FallingSandCell>,
    chunkPositions: ChunkPositions,
): ChangeCandidate {
    val currentIsSandRule = { logicChunk: ChunkPositions -> grid[logicChunk.current].type == CellType.Sand }
    val airExistsInAnyBelowDirection = { logicChunk: ChunkPositions ->
        CellType.Air in listOfNotNull(
            logicChunk.south,
            logicChunk.southEast,
            logicChunk.southWest
        ).map { grid[it].type }
    }
    val moveBelowToAnyFreeCellRule = { logicChunk: ChunkPositions ->
        listOfNotNull(
            logicChunk.south,
            logicChunk.southEast,
            logicChunk.southWest
        ).map { grid[it] }.filter { it.type == CellType.Air }
            .map(FallingSandCell::position)
            .firstOrNull()?.let { it - grid[logicChunk.current].position } ?: position(0, 0)
    }

    val change = when {
        currentIsSandRule(chunkPositions) && airExistsInAnyBelowDirection(chunkPositions) -> moveBelowToAnyFreeCellRule(
            chunkPositions
        )
        else -> position(0, 0)
    }
    val cell = grid[chunkPositions.current]
    return when (change) {
        position(0, 0) -> ChangeCandidate.Nothing
        else -> ChangeCandidate.Change(
            sourcePosition = chunkPositions.current,
            destinationPosition = cell.position.copy(
                row = cell.position.row + change.row,
                column = cell.position.column + change.column
            ),
            newType = CellType.Sand,
        )
    }
}