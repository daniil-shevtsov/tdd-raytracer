package fallingsand

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import assertk.assertions.support.expected
import canvas.Canvas
import canvas.color.color
import grid.Grid
import grid.toCanvas
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

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

        val updatedCell = getCellUpdate(grid, cell)

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
        val cell = fallingSandCell(position(0, 0), type = CellType.Sand)

        val updatedCell = getCellUpdate(grid, cell)

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
        val cell = fallingSandCell(position(0, 0), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(1, 0)

        val secondUpdate = getCellUpdate(grid, firstUpdate)
        assertThat(secondUpdate).hasPosition(2, 0)
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
        val cell = fallingSandCell(position(1, 1), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(2, 2)
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
        val cell = fallingSandCell(position(1, 1), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(2, 0)
    }

    @Test
    fun `should fall to the left side when sand on sand and right is screen edge`() {
        val grid = Grid.createInitialized(width = 3, height = 3) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 1 && column == 2 -> CellType.Sand
                    row == 2 && column == 2 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val cell = fallingSandCell(position(1, 2), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(2, 1)
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
        val cell = fallingSandCell(position(1, 1), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(1, 1)
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
        val cell = fallingSandCell(position(1, 0), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(2, 1)
    }

    private fun getCellUpdate(
        grid: Grid<FallingSandCell>,
        cell:FallingSandCell
    ): FallingSandCell {
        val cell = updateCell(grid, cell)
        return cell
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
        val cell = fallingSandCell(position(1, 0), type = CellType.Sand)

        val firstUpdate = getCellUpdate(grid, cell)
        assertThat(firstUpdate).hasPosition(1, 0)
    }

    @Test
    fun `should update every cell`() {
        val grid = Grid.createInitialized(width = 2, height = 2) { row, column ->
            fallingSandCell(
                position = position(row, column), type = when {
                    row == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            )
        }
        val newGrid = updateEveryCell(grid)

        assertThat(newGrid).hasTypes(
            position(0, 0) to CellType.Air,
            position(0, 1) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(1, 1) to CellType.Sand,
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
        val newGrid = updateEveryCell(grid)

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
    @Ignore
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
        val newGrid = updateEveryCell(grid)

        assertThat(newGrid).hasTypes(
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
            position(2, 2) to CellType.Sand,
            position(1, 0) to CellType.Air,
            position(1, 1) to CellType.Air,
            position(1, 2) to CellType.Sand,
        )
    }

    fun Assert<Grid<FallingSandCell>>.hasTypes(
        vararg expected: Pair<Position, CellType>,
    ) = given { actual ->
        val actualTypes = actual.getAsLists().flatMap { row ->
            row.map { cell ->
                cell.position to cell.type
            }
        }.toMap()
        val expectedTypes = expected.toMap()

        val difference = actualTypes.filter { expectedTypes[it.key] != null && expectedTypes[it.key] != it.value }

        if(difference.isEmpty()) {
            return@given
        }

        val expectedString = expectedTypes.toList().filter { difference.containsKey(it.first) }.joinToString(
            prefix = "{",
            postfix = "}",
            separator = "\n"
        ) { "[${it.first.row}:${it.first.column}] = ${it.second.name}" }

        val differenceString = difference.toList().joinToString(
            prefix = "{",
            postfix = "}",
            separator = "\n"
        ) { "[${it.first.row}:${it.first.column}] = ${it.second.name}" }

        expected(
            message = ":<$expectedString> but was:<$differenceString>",
            expected = expectedTypes,
            actual = actualTypes
        )
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

    private fun Assert<FallingSandCell>.hasPosition(row: Int, column: Int) {
        prop(FallingSandCell::position).all {
            prop(Position::column).isEqualTo(column)
            prop(Position::row).isEqualTo(row)
        }
    }

//    private fun Assert<ChangeCandidate>.hasPosition(row: Int, column: Int) {
//        isInstanceOf(ChangeCandidate.Change::class)
//        .prop(ChangeCandidate.Change::destinationPosition).all {
//            prop(Position::column).isEqualTo(column)
//            prop(Position::row).isEqualTo(row)
//        }
//    }

    private fun Assert<FallingSandCell>.hasType(type: CellType) {
        prop(FallingSandCell::type).isEqualTo(type)
    }

}