package fallingsand

import grid.Grid

fun applyChangesToEveryCell(
    grid: Grid<FallingSandCell>
): Grid<FallingSandCell> {
    var currentGrid = grid
    var handled = setOf<Position>()
    grid.positions.forEach { _ ->
        val changeCandidate = selectChangeCandidate(currentGrid, handled)
        currentGrid = applyChangeToGrid(
            grid = currentGrid,
            changeCandidate = changeCandidate
        )
        if (changeCandidate is ChangeCandidate.Change) {
            handled += changeCandidate.sourcePosition
        }
    }

    //return currentGrid
    val a = applyNextChangeToGrid(grid)
    val b = applyNextChangeToGrid(a)

    return b
}