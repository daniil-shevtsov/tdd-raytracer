package fallingsand

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import grid.Grid
import org.junit.jupiter.api.Test

internal class SelectChangeCandidateTest {

    @Test
    fun `should do nothing`() {
        val grid = Grid.createInitialized(size = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val nextChangeCandidate = selectChangeCandidate(grid)

        assertThat(nextChangeCandidate).isNothing()
    }

    @Test
    fun `should select first change candidate`() {
        val grid = Grid.createInitialized(size = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val nextChangeCandidate = selectChangeCandidate(grid)

        assertThat(nextChangeCandidate).all {
            currentPosition(0, 0)
            destinationPosition(1, 0)
            type(CellType.Sand)
        }
    }

    @Test
    fun `should select second change candidate`() {
        val grid = Grid.createInitialized(size = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 && column == 0 -> CellType.Sand
                    row == 1 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val nextChangeCandidate = selectChangeCandidate(
            grid = grid,
            handled = setOf(position(0, 0))
        )

        assertThat(nextChangeCandidate).all {
            currentPosition(1, 1)
            destinationPosition(2, 1)
            type(CellType.Sand)
        }
    }

    private fun Assert<ChangeCandidate>.currentPosition(row: Int, column: Int) {
        isChange()
            .prop(ChangeCandidate.Change::sourcePosition).isEqualTo(position(row = row, column = column))
    }

    private fun Assert<ChangeCandidate>.destinationPosition(row: Int, column: Int) {
        isChange()
            .prop(ChangeCandidate.Change::destinationPosition).isEqualTo(position(row = row, column = column))
    }

    private fun Assert<ChangeCandidate>.type(expected: CellType) {
        isChange()
            .prop(ChangeCandidate.Change::newType)
            .isEqualTo(expected)
    }

    private fun Assert<ChangeCandidate>.isChange() = isInstanceOf(ChangeCandidate.Change::class)
    private fun Assert<ChangeCandidate>.isNothing() = isInstanceOf(ChangeCandidate.Nothing::class)
}