package fallingsand

fun defaultSandGrid(size: Int = 2) = createFallingSandGrid(size = size, init = { _, _ -> CellType.Air })