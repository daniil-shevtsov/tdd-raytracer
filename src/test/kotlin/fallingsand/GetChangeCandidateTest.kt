package fallingsand

import assertk.assertThat
import assertk.assertions.isEqualTo
import grid.Grid
import org.junit.jupiter.api.Test

class GetChangeCandidateTest {

    @Test
    fun `should return nothing for empty grid`() {
        val candidate = createChangeCandidate(
            grid = Grid.createInitialized(size = 2, init = { _, _ -> fallingSandCell() }),
            chunkPositions = createChunkPositions(current = position(0, 0), width = 2, height = 2)
        )
        assertThat(candidate).isEqualTo(ChangeCandidate.Nothing)
    }

    @Test
    fun `should return nothing if only air`() {
        val candidate = createChangeCandidate(
            grid = Grid.createInitialized(size = 2, init = { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = CellType.Air,
                )
            }), chunkPositions = createChunkPositions(current = position(0, 0), width = 2, height = 2)
        )
        assertThat(candidate).isEqualTo(ChangeCandidate.Nothing)
    }

    @Test
    fun `should return nothing if sand at bottom`() {
        val candidate = createChangeCandidate(
            grid = Grid.createInitialized(size = 2, init = { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = when {
                        row == 1 -> CellType.Sand
                        else -> CellType.Air
                    },
                )
            }), chunkPositions = createChunkPositions(current = position(0, 0), width = 2, height = 2)
        )
        assertThat(candidate).isEqualTo(ChangeCandidate.Nothing)
    }

    @Test
    fun `should return falling sand if sand above air`() {
        val candidate = createChangeCandidate(
            grid = Grid.createInitialized(size = 2, init = { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = when {
                        row == 0 && column == 0 -> CellType.Sand
                        else -> CellType.Air
                    },
                )
            }), chunkPositions = createChunkPositions(current = position(0, 0), width = 2, height = 2)
        )
        assertThat(candidate).isEqualTo(
            ChangeCandidate.Change(
                sourcePosition = position(0, 0),
                destinationPosition = position(1, 0),
                newType = CellType.Sand
            )
        )
    }

    @Test
    fun `should return falling sand if sand below but air to southeast`() {
        val candidate = createChangeCandidate(
            grid = Grid.createInitialized(size = 2, init = { row, column ->
                fallingSandCell(
                    position = position(row, column),
                    type = when {
                        row == 0 && column == 0 -> CellType.Sand
                        row == 1 && column == 0 -> CellType.Sand
                        else -> CellType.Air
                    },
                )
            }), chunkPositions = createChunkPositions(current = position(0, 0), width = 2, height = 2)
        )
        assertThat(candidate).isEqualTo(
            ChangeCandidate.Change(
                sourcePosition = position(0, 0),
                destinationPosition = position(1, 1),
                newType = CellType.Sand
            )
        )
    }

    private fun createChangeCandidate(
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

//        val south = chunkPositions.south
//        return when {
//            grid[chunkPositions.current].type == CellType.Sand && south != null && grid[south].type == CellType.Air -> ChangeCandidate.Change(
//                sourcePosition = chunkPositions.current,
//                destinationPosition = south,
//                newType = CellType.Sand
//            )
//            else -> ChangeCandidate.Nothing
//        }
    }
}