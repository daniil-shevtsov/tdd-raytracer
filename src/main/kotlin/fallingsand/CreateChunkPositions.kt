package fallingsand

import org.jetbrains.annotations.TestOnly

data class ChunkPositions(
    val originalMap: Map<Direction, Position?>,
) {
    @TestOnly
    fun asMap() = originalMap.map {
        when {
            it.key.horizontal == HorizontalDirection.Center && it.key.vertical == VerticalDirection.Center -> "current"
            it.key.horizontal == HorizontalDirection.Center -> it.key.vertical.name.toLowerCase()
            it.key.vertical == VerticalDirection.Center -> it.key.horizontal.name.toLowerCase()
            else -> it.key.vertical.name.toLowerCase() + it.key.horizontal.name
        } to it.value
    }.toMap()

    val current: Position
        get() = originalMap[Direction(HorizontalDirection.Center, VerticalDirection.Center)]!!
    val south: Position?
        get() = originalMap[Direction(HorizontalDirection.Center, VerticalDirection.South)]
    val southEast: Position?
        get() = originalMap[Direction(HorizontalDirection.East, VerticalDirection.South)]
    val southWest: Position?
        get() = originalMap[Direction(HorizontalDirection.West, VerticalDirection.South)]

    companion object {
        fun fromDirections(
            current: Position,
            north: Position?,
            northEast: Position?,
            east: Position?,
            southEast: Position?,
            south: Position?,
            southWest: Position?,
            west: Position?,
            northWest: Position?,
        ): ChunkPositions = ChunkPositions(
            originalMap = mapOf(
                Direction(HorizontalDirection.Center, VerticalDirection.Center) to current,
                Direction(HorizontalDirection.Center, VerticalDirection.North) to north,
                Direction(HorizontalDirection.East, VerticalDirection.North) to northEast,
                Direction(HorizontalDirection.East, VerticalDirection.Center) to east,
                Direction(HorizontalDirection.East, VerticalDirection.South) to southEast,
                Direction(HorizontalDirection.Center, VerticalDirection.South) to south,
                Direction(HorizontalDirection.West, VerticalDirection.South) to southWest,
                Direction(HorizontalDirection.West, VerticalDirection.Center) to west,
                Direction(HorizontalDirection.West, VerticalDirection.North) to northWest,
            )
        )
    }
}

@TestOnly
fun chunkPositions(originalMap: Map<Direction, Position?> = mapOf()) = ChunkPositions(
    originalMap = originalMap,
)

data class Direction(
    val horizontal: HorizontalDirection,
    val vertical: VerticalDirection,
)

enum class HorizontalDirection {
    West, Center, East
}

enum class VerticalDirection {
    North, Center, South
}

fun createChunkPositions(current: Position, width: Int, height: Int): ChunkPositions {
    val min = position(0, 0)
    val max = position(height - 1, width - 1)

    val map = HorizontalDirection.values().flatMap { horizontal ->
        VerticalDirection.values().map { vertical ->
            Direction(horizontal, vertical)
        }
    }.associateWith { direction ->
        val horizontalOffset = when (direction.horizontal) {
            HorizontalDirection.West -> -1
            HorizontalDirection.Center -> 0
            HorizontalDirection.East -> 1
        }
        val verticalOffset = when (direction.vertical) {
            VerticalDirection.North -> -1
            VerticalDirection.Center -> 0
            VerticalDirection.South -> 1
        }
        position(verticalOffset, horizontalOffset)
    }

    return ChunkPositions(
        originalMap = map.mapValues { (current + it.value).takeIf { it.row >= min.row && it.row <= max.row && it.column >= min.column && it.column <= max.column } },
    )
}