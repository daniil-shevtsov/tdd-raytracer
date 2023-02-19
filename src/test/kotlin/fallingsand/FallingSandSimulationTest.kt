package fallingsand

import assertk.Assert
import assertk.assertThat
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class FallingSandSimulationTest {

    @Test
    fun `should create air`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(size = 2) { row, column ->
                CellType.Sand
            }
        )
        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.CreateAir(row = 1, column = 1),
        )
        assertThat(simulated).hasTypes(
            position(0, 0) to CellType.Sand,
            position(1, 1) to CellType.Air,
        )
    }

    @Test
    fun `should create sand`() {
        val initialState = fallingSandSimulationState(
            grid = createFallingSandGrid(size = 2) { row, column ->
                CellType.Air
            }
        )
        val simulated = fallingSandSimulation(
            currentState = initialState,
            action = FallingSandAction.CreateSand(row = 0, column = 1),
        )
        assertThat(simulated).hasTypes(
            position(1, 1) to CellType.Air,
            position(0, 1) to CellType.Sand,
        )
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

    private fun Assert<FallingSandSimulationState>.hasTypes(
        vararg expected: Pair<Position, CellType>,
    ) = prop(FallingSandSimulationState::grid).hasTypes(*expected)

}