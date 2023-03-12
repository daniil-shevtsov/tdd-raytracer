package fallingsand

import grid.Grid

fun applyChangesToEveryCell(
    grid: Grid<FallingSandCell>
): Grid<FallingSandCell> {
    var currentGrid = grid
    var handled = setOf<Position>()
    var changesToApply = emptyList<ChangeCandidate>()

    repeat(grid.width * grid.height) {
        val changeCandidate = selectChangeCandidate(currentGrid, handled)
//        currentGrid = applyChangeToGrid(
//            grid = currentGrid,
//            changeCandidate = changeCandidate
//        )
        if (changeCandidate is ChangeCandidate.Change) {
            handled += changeCandidate.sourcePosition
            changesToApply = changesToApply + changeCandidate
        }
    }

    changesToApply.forEach { changeCandidate ->
        currentGrid = applyChangeToGrid(
            grid = currentGrid,
            changeCandidate = changeCandidate,
        )
    }

    return currentGrid
}