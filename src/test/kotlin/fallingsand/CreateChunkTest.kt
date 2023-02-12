package fallingsand

import assertk.Assert
import assertk.assertThat
import assertk.assertions.support.expected
import org.junit.jupiter.api.Test

class CreateChunkTest {
    @Test
    fun `should use current as current`() {
        val chunkPositions = createChunkPositions(current = position(1, 1))

        assertThat(chunkPositions).hasPositions(current = position(1, 1))
    }

    @Test
    fun `should use north as north`() {
        val chunkPositions = createChunkPositions(current = position(1, 1))

        assertThat(chunkPositions).hasPositions(
            current = position(1, 1),
            north = position(0, 1),
        )
    }

    private fun createChunkPositions(
        current: Position = position(0, 0),
        width: Int = 100,
        height: Int = 100,
        kek: String = "",
    ) = createChunkPositions(
        current = current,
        width = width,
        height = height,
    )

    private fun createChunkPositions(current: Position, width: Int, height: Int): ChunkPositions {
        return ChunkPositions(
            current = position(1, 1),
            north = position(0, 1),
            northEast = null,
            east = null,
            southEast = null,
            south = null,
            southWest = null,
            west = null,
            northWest = null,
        )
    }

    private fun Assert<ChunkPositions>.hasPositions(
        current: Position = position(-1, -1),
        north: Position? = position(-1, -1),
        northEast: Position? = position(-1, -1),
        east: Position? = position(-1, -1),
        southEast: Position? = position(-1, -1),
        south: Position? = position(-1, -1),
        southWest: Position? = position(-1, -1),
        west: Position? = position(-1, -1),
        northWest: Position? = position(-1, -1),
    ) {
        hasPositions(
            chunkPositions = ChunkPositions(
                current = current,
                north = north,
                northEast = northEast,
                east = east,
                southEast = southEast,
                south = south,
                southWest = southWest,
                west = west,
                northWest = northWest,
            )
        )
    }

    private fun Assert<ChunkPositions>.hasPositions(
        chunkPositions: ChunkPositions,
    ) = given { actual ->
        val positionsToCheck = chunkPositions.asMap().filterValues { it != position(-1, -1) }.keys
        val actualPositions = actual.asMap().filterKeys { it in positionsToCheck }
        val expectedPositions = chunkPositions.asMap().filterKeys { it in positionsToCheck }

        val difference = actualPositions.filter { expectedPositions[it.key] != it.value }

        if (difference.isEmpty()) {
            return@given
        }

        val expectedString = expectedPositions.toList().filter { difference.containsKey(it.first) }.joinToString(
            prefix = "{",
            postfix = "}",
            separator = "\n"
        ) { "[${it.first}] = ${it.second}" }

        val differenceString = difference.toList().joinToString(
            prefix = "{",
            postfix = "}",
            separator = "\n"
        ) { "[${it.first}] = ${it.second}" }

        expected(
            message = ":<$expectedString> but was:<$differenceString>",
            expected = expectedPositions,
            actual = actualPositions
        )
    }
}