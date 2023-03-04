package fallingsand

import grid.Grid

fun selectChangeCandidate(
    grid: Grid<FallingSandCell>,
    handled: Set<Position> = setOf(),
): ChangeCandidate {
    val changeCandidates = grid.positions.filter { it !in handled }.map { position ->
        createChangeCandidate(
            grid = grid,
            chunkPositions = createChunkPositions(current = position, width = grid.width, height = grid.height)
        )
    }
    val changes = changeCandidates.filterIsInstance<ChangeCandidate.Change>()
    return when (changes.isNotEmpty()) {
        true -> changes.first()
        false -> ChangeCandidate.Nothing
    }
}

fun createChangeCandidate(
    grid: Grid<FallingSandCell>,
    chunkPositions: ChunkPositions,
): ChangeCandidate {
    val airAt = { position: Position? -> position != null && grid[position].type == CellType.Air }
    val sandAt = { position: Position? -> position != null && grid[position].type == CellType.Sand }

    val currentIsSandRule = { logicChunk: ChunkPositions -> sandAt(logicChunk.current) }
    val freeAirExistsInAnyBelowDirection = { logicChunk: ChunkPositions ->
        when {
            airAt(logicChunk.south) -> true
            airAt(logicChunk.southEast) && airAt(logicChunk.east) -> true
            airAt(logicChunk.southWest) && airAt(logicChunk.west) -> true
            else -> false
        }
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
        currentIsSandRule(chunkPositions) && freeAirExistsInAnyBelowDirection(chunkPositions) -> moveBelowToAnyFreeCellRule(
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