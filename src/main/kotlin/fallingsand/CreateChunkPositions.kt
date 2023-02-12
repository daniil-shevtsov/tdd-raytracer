package fallingsand

import org.jetbrains.annotations.TestOnly

data class ChunkPositions(
    val currentt: Position,
    val north: Position?,
    val northEast: Position?,
    val east: Position?,
    val southEast: Position?,
    val south: Position?,
    val southWest: Position?,
    val west: Position?,
    val northWest: Position?,
    val originalMap: Map<Direction, Position?>,
) {
    @TestOnly
    fun asMap() = listOf(
        "current" to current,
        "north" to north,
        "northEast" to northEast,
        "east" to east,
        "southEast" to southEast,
        "south" to south,
        "southWest" to southWest,
        "west" to west,
        "northWest" to northWest,
    ).toMap()

    val current: Position
        get() = originalMap[Direction(HorizontalDirection.Center, VerticalDirection.Center)]!!

    val map: Map<Direction, Position?>
        get() = mapOf(
            Direction(HorizontalDirection.Center, VerticalDirection.Center) to current,
            Direction(HorizontalDirection.Center, VerticalDirection.North) to north,
            Direction(HorizontalDirection.East, VerticalDirection.North) to northEast,
            Direction(HorizontalDirection.East, VerticalDirection.Center) to east,
            Direction(HorizontalDirection.East, VerticalDirection.South) to southEast,
            Direction(HorizontalDirection.Center, VerticalDirection.South) to south,
            Direction(HorizontalDirection.West, VerticalDirection.South) to southWest,
            Direction(HorizontalDirection.West, VerticalDirection.Center) to west,
            Direction(HorizontalDirection.West, VerticalDirection.North) to west,
        )

    fun get(direction: Direction): Position? = map[direction]

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
            currentt = current,
            north = north,
            northEast = northEast,
            east = east,
            southEast = southEast,
            south = south,
            southWest = southWest,
            west = west,
            northWest = northWest,
            originalMap = mapOf(
                Direction(HorizontalDirection.Center, VerticalDirection.Center) to current,
                Direction(HorizontalDirection.Center, VerticalDirection.North) to north,
                Direction(HorizontalDirection.East, VerticalDirection.North) to northEast,
                Direction(HorizontalDirection.East, VerticalDirection.Center) to east,
                Direction(HorizontalDirection.East, VerticalDirection.South) to southEast,
                Direction(HorizontalDirection.Center, VerticalDirection.South) to south,
                Direction(HorizontalDirection.West, VerticalDirection.South) to southWest,
                Direction(HorizontalDirection.West, VerticalDirection.Center) to west,
                Direction(HorizontalDirection.West, VerticalDirection.North) to west,
            )
        )
    }
}

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
        currentt = current,
        north = (current - position(1, 0)).takeIf { it.row >= min.row },
        northEast = (current + position(-1, 1)).takeIf { it.row >= min.row && it.column <= max.column },
        east = (current + position(0, 1)).takeIf { it.column <= max.column },
        southEast = (current + position(1, 1)).takeIf { it.row <= max.row && it.column <= max.column },
        south = (current + position(1, 0)).takeIf { it.row <= max.row },
        southWest = (current + position(1, -1)).takeIf { it.row <= max.row && it.column >= min.column },
        west = (current - position(0, 1)).takeIf { it.column >= min.column },
        northWest = (current - position(1, 1)).takeIf { it.row >= min.row && it.column >= min.column },
        originalMap = map.mapValues { (current + it.value).takeIf { it.row >= min.row && it.row <= max.row && it.column >= min.column && it.column <= max.column } },
    )
}