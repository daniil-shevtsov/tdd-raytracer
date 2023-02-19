package fallingsand

fun defaultSandGrid(size: Int = 2) = filledSandGrid(
    size = size,
    cellType = CellType.Air,
)

fun filledSandGrid(
    size: Int = 2,
    cellType: CellType
) = createFallingSandGrid(size = size, init = { _, _ -> cellType })