package fallingsand

import org.jetbrains.annotations.TestOnly

data class FallingSandCell(
    val position: Position,
)

data class Position(
    val row: Int,
    val column: Int,
)

@TestOnly
fun fallingSandCell(position: Position = position()) = FallingSandCell(position = position)

@TestOnly
fun position(row: Int = 0, column: Int = 0) = Position(row = row, column = column)