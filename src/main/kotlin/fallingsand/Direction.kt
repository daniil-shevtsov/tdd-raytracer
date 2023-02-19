package fallingsand

import org.jetbrains.annotations.TestOnly

data class Direction(
    val horizontal: HorizontalDirection,
    val vertical: VerticalDirection,
)

fun Direction.toPositionOffset(): Position {
    val horizontalOffset = when (horizontal) {
        HorizontalDirection.West -> -1
        HorizontalDirection.Center -> 0
        HorizontalDirection.East -> 1
    }
    val verticalOffset = when (vertical) {
        VerticalDirection.North -> -1
        VerticalDirection.Center -> 0
        VerticalDirection.South -> 1
    }
    return position(verticalOffset, horizontalOffset)
}

@TestOnly
fun direction(
    horizontal: HorizontalDirection = HorizontalDirection.Center,
    vertical: VerticalDirection = VerticalDirection.Center,
) = Direction(
    horizontal = horizontal,
    vertical = vertical,
)

enum class HorizontalDirection {
    West, Center, East
}

enum class VerticalDirection {
    North, Center, South
}