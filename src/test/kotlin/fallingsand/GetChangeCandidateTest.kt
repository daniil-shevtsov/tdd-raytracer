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
}