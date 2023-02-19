package fallingsand

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class DirectionTest {
    @Test
    fun `should stay at place when center`() {
        val direction = direction(HorizontalDirection.Center, VerticalDirection.Center)

        assertThat(direction.toPositionOffset()).isEqualTo(position(0, 0))
    }

    @Test
    fun `should move horizontally`() {
        val west = direction(HorizontalDirection.West, VerticalDirection.Center)
        val east = direction(HorizontalDirection.East, VerticalDirection.Center)

        assertThat(east.toPositionOffset()).isEqualTo(position(0, 1))
        assertThat(west.toPositionOffset()).isEqualTo(position(0, -1))
    }

    @Test
    fun `should move vertically`() {
        val north = direction(HorizontalDirection.Center, VerticalDirection.North)
        val south = direction(HorizontalDirection.Center, VerticalDirection.South)

        assertThat(north.toPositionOffset()).isEqualTo(position(-1, 0))
        assertThat(south.toPositionOffset()).isEqualTo(position(1, 0))
    }

    @Test
    fun `should move diagonally`() {
        val northEast = direction(HorizontalDirection.East, VerticalDirection.North)
        val southEast = direction(HorizontalDirection.East, VerticalDirection.South)
        val southWest = direction(HorizontalDirection.West, VerticalDirection.South)
        val northWest = direction(HorizontalDirection.West, VerticalDirection.North)

        assertThat(northEast.toPositionOffset()).isEqualTo(position(-1, 1))
        assertThat(southEast.toPositionOffset()).isEqualTo(position(1, 1))
        assertThat(southWest.toPositionOffset()).isEqualTo(position(1, -1))
        assertThat(northWest.toPositionOffset()).isEqualTo(position(-1, -1))
    }
}