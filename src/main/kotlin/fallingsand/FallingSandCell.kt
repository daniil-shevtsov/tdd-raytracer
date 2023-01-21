package fallingsand

import org.jetbrains.annotations.TestOnly

data class FallingSandCell(
    val position: Position,
    val type: CellType,
)

enum class CellType {
    Air,
    Sand,
}

data class Position(
    val row: Int,
    val column: Int,
)

@TestOnly
fun fallingSandCell(
    position: Position = position(),
    type: CellType = CellType.Air,
) = FallingSandCell(position = position, type = type)

@TestOnly
fun position(row: Int = 0, column: Int = 0) = Position(row = row, column = column)