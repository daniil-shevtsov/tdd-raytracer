package fallingsand

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import canvas.Canvas
import canvas.color.color
import grid.Grid
import grid.toCanvas
import org.junit.jupiter.api.Test

class GridTest {
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
    fun `should map grid to canvas`() {
        val grid = Grid.createInitialized(width = 2, height = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == column -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val canvas = grid.toCanvas()

        assertThat(canvas).prop(Canvas::pixels).containsExactly(
            listOf(color(0, 0, 0), color(1, 1, 1)),
            listOf(color(1, 1, 1), color(0, 0, 0)),
        )
    }
}