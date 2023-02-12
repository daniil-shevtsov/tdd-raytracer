package fallingsand

import org.jetbrains.annotations.TestOnly

data class ChunkPositions(
    val current: Position,
    val north: Position?,
    val northEast: Position?,
    val east: Position?,
    val southEast: Position?,
    val south: Position?,
    val southWest: Position?,
    val west: Position?,
    val northWest: Position?,
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
}

fun createChunkPositions(current: Position, width: Int, height: Int): ChunkPositions {
    val min = position(0, 0)
    val max = position(height - 1, width - 1)
    return ChunkPositions(
        current = current,
        north = (current - position(1, 0)).takeIf { it.row >= min.row },
        northEast = (current + position(-1, 1)).takeIf { it.row >= min.row && it.column <= max.column },
        east = (current + position(0, 1)).takeIf { it.column <= max.column },
        southEast = (current + position(1, 1)).takeIf { it.row <= max.row && it.column <= max.column },
        south = (current + position(1, 0)).takeIf { it.row <= max.row },
        southWest = (current + position(1, -1)).takeIf { it.row <= max.row && it.column >= min.column },
        west = (current - position(0, 1)).takeIf { it.column >= min.column },
        northWest = (current - position(1, 1)).takeIf { it.row >= min.row && it.column >= min.column },
    )
}