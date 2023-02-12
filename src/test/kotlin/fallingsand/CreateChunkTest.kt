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

    @Test
    fun `should set every side for current 1 1`() {
        val chunkPositions = createChunkPositions(current = position(1, 1))

        assertThat(chunkPositions).hasPositions(
            current = position(1, 1),
            north = position(0, 1),
            northEast = position(0, 2),
            east = position(1, 2),
            southEast = position(2, 2),
            south = position(2, 1),
            southWest = position(2, 0),
            west = position(1, 0),
            northWest = position(0, 0),
        )
    }

    @Test
    fun `should set every side for current 2 2`() {
        val chunkPositions = createChunkPositions(current = position(2, 2))

        assertThat(chunkPositions).hasPositions(
            current = position(2, 2),
            north = position(1, 2),
            northEast = position(1, 3),
            east = position(2, 3),
            southEast = position(3, 3),
            south = position(3, 2),
            southWest = position(3, 1),
            west = position(2, 1),
            northWest = position(1, 1),
        )
    }

    @Test
    fun `should set north as null when northest row`() {
        val chunkPositions = createChunkPositions(current = position(0, 2))

        assertThat(chunkPositions).hasPositions(
            current = position(0, 2),
            north = null,
            northEast = null,
            northWest = null,
        )
    }

    @Test
    fun `should set east as null when eastest column`() {
        val chunkPositions = createChunkPositions(
            current = position(1, 2),
            width = 3,
        )

        assertThat(chunkPositions).hasPositions(
            current = position(1, 2),
            northEast = null,
            east = null,
            southEast = null,
        )
    }

    @Test
    fun `should set south as null when southest row`() {
        val chunkPositions = createChunkPositions(
            current = position(2, 1),
            height = 3,
        )

        assertThat(chunkPositions).hasPositions(
            current = position(2, 1),
            southEast = null,
            south = null,
            southWest = null,
        )
    }

    @Test
    fun `should set west as null when westest column`() {
        val chunkPositions = createChunkPositions(
            current = position(1, 0),
            width = 3,
        )

        assertThat(chunkPositions).hasPositions(
            current = position(1, 0),
            southWest = null,
            west = null,
            northWest = null,
        )
    }

    private fun createChunkPositions(
        current: Position = position(0, 0),
        width: Int = 100,
        height: Int = 100,
        kek: String = "",
    ) = fallingsand.createChunkPositions(current = current, width = width, height = height)

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
            chunkPositions = ChunkPositions.fromDirections(
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