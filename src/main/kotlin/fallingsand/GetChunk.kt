package fallingsand

import grid.Grid

data class LogicChunk(
    val south: FallingSandCell?,
    val southEast: FallingSandCell?,
    val southWest: FallingSandCell?,
    val current: FallingSandCell,
)

fun createLogicChunk(grid: Grid<FallingSandCell>, cell: FallingSandCell): LogicChunk {
    return LogicChunk(
        south = when (cell.position.row) {
            grid.height - 1 -> null
            else -> grid[cell.position.row + 1, cell.position.column]
        },
        southEast = when {
            cell.position.row == grid.height - 1 || cell.position.column == grid.width - 1 -> null
            else -> grid[cell.position.row + 1, cell.position.column + 1]
        },
        southWest = when {
            cell.position.row == grid.height - 1 || cell.position.column == 0 -> null
            else -> grid[cell.position.row + 1, cell.position.column - 1]
        },
        current = cell,
    )
}