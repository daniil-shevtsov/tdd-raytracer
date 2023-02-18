package fallingsand

import assertk.assertThat
import grid.Grid
import org.junit.jupiter.api.Test

class ApplyNextChangeTest {
    @Test
    fun `sand should stay at the bottom`() {
        val grid = Grid.createInitialized(width = 2, height = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 0) to CellType.Sand
        )
    }

    @Test
    fun `sand should fall when one pixel up in the air`() {
        val grid = Grid.createInitialized(width = 2, height = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(0, 0) to CellType.Air,
            position(1, 0) to CellType.Sand,
        )
    }

    @Test
    fun `sand should fall when two pixels in the air`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        val gridAfterFirstUpdate = applyNextChangeToGrid(grid)
        assertThat(gridAfterFirstUpdate).hasTypes(
            position(0, 0) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(2, 0) to CellType.Air,
        )
        val gridAfterSecondUpdate = applyNextChangeToGrid(gridAfterFirstUpdate)
        assertThat(gridAfterSecondUpdate).hasTypes(
            position(0, 0) to CellType.Air,
            position(1, 0) to CellType.Air,
            position(2, 0) to CellType.Sand,
        )
    }

    @Test
    fun `should fall to the right side when sand on sand`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 1 -> CellType.Sand
                    row == 2 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 1) to CellType.Air,
            position(2, 1) to CellType.Sand,
            position(2, 2) to CellType.Sand,
        )
    }

    @Test
    fun `should fall to the left side when sand on sand and right already sand`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 1 -> CellType.Sand
                    row == 2 && column == 1 -> CellType.Sand
                    row == 2 && column == 2 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 1) to CellType.Air,
            position(2, 1) to CellType.Sand,
            position(2, 2) to CellType.Sand,
            position(2, 0) to CellType.Sand,
        )
    }

    @Test
    fun `should fall to the left side when sand on sand and right is screen edge`() {
        val grid = Grid.createInitialized(size = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 2 -> CellType.Sand
                    row == 2 && column == 2 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }

        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 2) to CellType.Air,
            position(2, 2) to CellType.Sand,
            position(2, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should stay when sand on sand and left and right already sand`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 2 && column == 2 -> CellType.Sand
                    row == 2 && column == 1 -> CellType.Sand
                    row == 2 && column == 0 -> CellType.Sand
                    row == 1 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(2, 2) to CellType.Sand,
            position(2, 1) to CellType.Sand,
            position(2, 0) to CellType.Sand,
            position(1, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should fall to the right side when sand on sand and left is screen edge`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 0 -> CellType.Sand
                    row == 2 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 0) to CellType.Air,
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should stay when sand on sand and left is screen edge and right already sand`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 0 -> CellType.Sand
                    row == 2 && column == 0 -> CellType.Sand
                    row == 2 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        assertThat(applyNextChangeToGrid(grid)).hasTypes(
            position(1, 0) to CellType.Sand,
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should only one fall when air under and two candidate with grid`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 0 -> CellType.Sand
                    row == 1 && column == 2 -> CellType.Sand
                    row == 2 && column == 1 -> CellType.Air
                    row == 2 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val newGrid = applyNextChangeToGrid(grid)

        assertThat(newGrid).hasTypes(
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
            position(2, 2) to CellType.Sand,
            position(1, 0) to CellType.Air,
            position(1, 1) to CellType.Air,
            position(1, 2) to CellType.Sand,
        )
    }

    @Test
    fun `should create change candidate`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 2 && column == 1 -> CellType.Air
                    row == 1 && column == 0 -> CellType.Sand
                    row == 1 && column == 2 -> CellType.Sand
                    row == 2 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val newGrid = applyNextChangeToGrid(grid)

        assertThat(newGrid).hasTypes(
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
            position(2, 2) to CellType.Sand,
            position(1, 0) to CellType.Air,
            position(1, 1) to CellType.Air,
            position(1, 2) to CellType.Sand,
        )
    }
}