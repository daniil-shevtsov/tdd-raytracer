package fallingsand

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import assertk.assertions.support.expected
import org.junit.jupiter.api.Test

internal class FallingSandSimulationTest {

    @Test
    fun `should spawn air at origin`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(size = 2) { row, column ->
                CellType.Sand
            }
        )
        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.Spawn(cellType = CellType.Air),
        )
        assertThat(simulated).hasTypes(
            position(0, 0) to CellType.Air,
            position(1, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should spawn sand at origin`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(size = 2) { row, column ->
                CellType.Air
            }
        )

        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.Spawn(cellType = CellType.Sand),
        )
        assertThat(simulated).hasTypes(
            position(0, 0) to CellType.Sand,
            position(1, 1) to CellType.Air,
        )
    }

    @Test
    fun `should move cursor`() {
        val initialState = fallingSandSimulationState(
            grid = defaultSandGrid(size = 2)
        )

        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.MoveCursor(
                direction = direction(
                    horizontal = HorizontalDirection.East,
                    vertical = VerticalDirection.South,
                )
            )
        )

        assertThat(simulated).hasCursorAt(position(row = 1, column = 1))
    }

    @Test
    fun `should spawn at cursor position`() {
        val initialState = fallingSandSimulationState(
            grid = filledSandGrid(size = 2, cellType = CellType.Air)
        )

        val cursorMovedState = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.MoveCursor(
                direction = direction(
                    horizontal = HorizontalDirection.East,
                    vertical = VerticalDirection.South,
                )
            )
        )

        val materialSpawnedState = fallingSandSimulation(
            currentState = cursorMovedState,
            action = FallingSandAction.Spawn(cellType = CellType.Sand)
        )

        assertThat(materialSpawnedState).hasTypes(position(1, 1) to CellType.Sand)
    }

    @Test
    fun `should tick`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(size = 2) { row, column ->
                when {
                    row == 0 && column == 0 -> CellType.Sand
                    else -> CellType.Air
                }
            }
        )

        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.Tick,
        )
        assertThat(simulated).hasTypes(
            position(0, 0) to CellType.Air,
            position(1, 0) to CellType.Sand,
        )
    }

    @Test
    fun `should handle one step of every cell per tick`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(width = 2, height = 3) { row, column ->
                when {
                    row == 0 && column == 0 -> CellType.Sand
                    row == 0 && column == 1 -> CellType.Sand
                    else -> CellType.Air
                }
            }
        )

        val afterFirstTick = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.Tick,
        )
        val afterSecondTick = fallingSandSimulation(
            currentState = afterFirstTick,
            action = FallingSandAction.Tick,
        )
        assertThat(afterFirstTick).hasTypes(
            position(0, 0) to CellType.Air,
            position(0, 1) to CellType.Air,
            position(1, 0) to CellType.Sand,
            position(1, 1) to CellType.Sand,
        )
        assertThat(afterSecondTick).hasTypes(
            position(1, 0) to CellType.Air,
            position(1, 1) to CellType.Air,
            position(2, 0) to CellType.Sand,
            position(2, 1) to CellType.Sand,
        )
    }

    @Test
    fun `should toggle pause on - off`() {
        val state = fallingSandSimulation(
            currentState = fallingSandSimulationState(isPaused = true),
            action = FallingSandAction.TogglePause
        )
        assertThat(state).isNotPaused()
    }

    @Test
    fun `should toggle pause off - on`() {
        val state = fallingSandSimulation(
            currentState = fallingSandSimulationState(isPaused = false),
            action = FallingSandAction.TogglePause
        )
        assertThat(state).isPaused()
    }

    private fun Assert<FallingSandSimulationState>.hasTypes(
        vararg expected: Pair<Position, CellType>,
    ) = prop(FallingSandSimulationState::grid).hasTypes(*expected)

    private fun Assert<FallingSandSimulationState>.isPaused() = paused(expected = true)
    private fun Assert<FallingSandSimulationState>.isNotPaused() = paused(expected = false)

    private fun Assert<FallingSandSimulationState>.paused(
        expected: Boolean
    ) = prop(FallingSandSimulationState::isPaused).isEqualTo(expected)
}

private fun Assert<FallingSandSimulationState>.hasCursorAt(expected: Position) = given { actual ->
    val actualPosition = actual.cursorPosition

    val difference = actualPosition - expected

    if (difference == position(0, 0)) {
        return@given
    }
    val positionString = { position: Position -> "(row=${position.row}, column=${position.column})" }

    expected(
        message = ":<${positionString(expected)}> but was:<${positionString(actualPosition)}>",
        expected = expected,
        actual = actualPosition
    )
}
