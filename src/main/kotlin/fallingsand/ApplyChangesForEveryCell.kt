package fallingsand

import grid.Grid

fun applyChangesToEveryCell(grid: Grid<FallingSandCell>): Grid<FallingSandCell> {
    val a = applyNextChangeToGrid(grid)
    val b = applyNextChangeToGrid(a)

    return b
}