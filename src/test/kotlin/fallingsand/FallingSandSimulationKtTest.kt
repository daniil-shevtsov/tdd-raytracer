package fallingsand

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

}