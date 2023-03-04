package fallingsand

import assertk.assertThat
import grid.Grid
import org.junit.jupiter.api.Test

class ApplyChangesForEveryCellTest {
    @Test
    fun `should apply for all cells `() {
        val grid = Grid.createInitialized(width = 2, height = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    row == 0 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyChangesToEveryCell(grid)).hasTypes(
            position(0, 0) to CellType.Air,
            position(0, 1) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(1, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should not apply change for one cell two times before all others `() {
        val grid = Grid.createInitialized(width = 2, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    row == 0 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyChangesToEveryCell(grid)).hasTypes(
            position(0, 0) to CellType.Air,
            position(0, 1) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(1, 1) to CellType.Sand,
            position(2, 1) to CellType.Air,
            position(2, 1) to CellType.Air,
        )
    }

    @Test
    fun `should not apply change for one cell two times before all others when diagonal`() {
        val grid = Grid.createInitialized(width = 2, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    row == 1 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyChangesToEveryCell(grid)).hasTypes(
            position(0, 0) to CellType.Air,
            position(0, 1) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(1, 1) to CellType.Air,
            position(2, 0) to CellType.Air,
            position(2, 1) to CellType.Sand,
        )
    }
}