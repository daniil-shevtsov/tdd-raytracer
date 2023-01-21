package fallingsand

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import grid.Grid
import org.junit.jupiter.api.Test

internal class FallingSandSimulationKtTest {

    @Test
    fun `should initialize grid`() {
        val grid = Grid.createInitialized(size = 2) { row, column ->
            row + column
        }
        assertThat(grid[0, 0]).isEqualTo(0)
        assertThat(grid[0, 1]).isEqualTo(1)
        assertThat(grid[1, 0]).isEqualTo(1)
        assertThat(grid[1, 1]).isEqualTo(2)
    }

    @Test
    fun `should initialize grid with cells`() {
        val grid = Grid.createInitialized(size = 2) { row, column ->
            fallingSandCell(position = position(row, column))
        }
        assertThat(grid[0, 0]).prop(FallingSandCell::position).isEqualTo(position(0, 0))
        assertThat(grid[0, 1]).prop(FallingSandCell::position).isEqualTo(position(0, 1))
        assertThat(grid[1, 0]).prop(FallingSandCell::position).isEqualTo(position(1, 0))
        assertThat(grid[1, 1]).prop(FallingSandCell::position).isEqualTo(position(1, 1))
    }

    @Test
    fun `should update grid`() {
        val grid = Grid.createInitialized(size = 2) { row, column ->
            row + column
        }

        val updatedGrid = grid.update { row, column, value ->
            value + row + column
        }

        assertThat(updatedGrid[0, 0]).isEqualTo(0)
        assertThat(updatedGrid[0, 1]).isEqualTo(2)
        assertThat(updatedGrid[1, 0]).isEqualTo(2)
        assertThat(updatedGrid[1, 1]).isEqualTo(4)
    }

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
        val cell = fallingSandCell(position(1, 0))

        val updatedCell = updateCell(grid, cell)

        assertThat(updatedCell).hasPosition(1, 0)
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
        val cell = fallingSandCell(position(0, 0))

        val updatedCell = updateCell(grid, cell)

        assertThat(updatedCell).hasPosition(1, 0)
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
        val cell = fallingSandCell(position(0, 0))

        val firstUpdate = updateCell(grid, cell)
        assertThat(firstUpdate).hasPosition(1, 0)

        val secondUpdate = updateCell(grid, firstUpdate)
        assertThat(secondUpdate).hasPosition(2, 0)
    }

    fun updateCell(grid: Grid<FallingSandCell>, cell: FallingSandCell): FallingSandCell {
        //val cellBelow = grid[cell.position.row + 1, cell.position.column]
        return when {
            grid.height - 1 == cell.position.row -> cell
            else -> cell.copy(position = cell.position.copy(row = cell.position.row + 1))
        }
    }

    private fun Assert<FallingSandCell>.hasPosition(row: Int, column: Int) {
        prop(FallingSandCell::position).all {
            prop(Position::column).isEqualTo(column)
            prop(Position::row).isEqualTo(row)
        }
    }

}