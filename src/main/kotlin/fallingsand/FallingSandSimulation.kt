package fallingsand

import grid.Grid

fun fallingSandSimulation(
    currentGrid: Grid<FallingSandCell>
): Grid<FallingSandCell> {
    return updateEveryCell(currentGrid)
}

fun updateEveryCell(grid: Grid<FallingSandCell>): Grid<FallingSandCell> {
    val updatedGrid = grid.update { row, column, value ->
        updateCell(grid, value)
    }.getAsLists()
        .flatten()
        .let { originalList ->
            val map = mutableMapOf<Position, List<FallingSandCell>>()
            originalList.forEach { cell ->
                map[cell.position] = map[cell.position].orEmpty() + cell
            }
            map.toMap()
        }
    return grid.update { row, column, value ->
        when (val cell = updatedGrid[position(row, column)].orEmpty().firstOrNull { it.type != CellType.Air }
            ?: updatedGrid[position(row, column)]?.first()) {
            null -> value.copy(type = CellType.Air)
            else -> cell
        }
    }
}

fun updateCell(grid: Grid<FallingSandCell>, cell: FallingSandCell): FallingSandCell {
    //val cellBelow = grid[cell.position.row + 1, cell.position.column]
    return when (cell.type) {
        CellType.Sand -> when (cell.position.row) {
            grid.height - 1 -> cell
            else -> cell.copy(position = cell.position.copy(row = cell.position.row + 1))
        }
        CellType.Air -> cell
    }
}