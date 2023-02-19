package fallingsand

fun defaultSandGrid() = createFallingSandGrid(size = 2, init = { _, _ -> CellType.Air })