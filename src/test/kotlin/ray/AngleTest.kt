package ray

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class AngleTest {

    @Test
    fun `0 degrees is 0 radians`() {
        assertThat(degrees(0f).toRadians()).isEqualTo(radians(0f))
    }

    @Test
    fun `0 radians is 0 degrees`() {
        assertThat(radians(0f).toDegrees()).isEqualTo(degrees(0f))
    }

    @Test
    fun `360 degrees is 2 pi radians`() {
        assertThat(degrees(360f).toRadians()).isEqualTo(radians(2f * Math.PI.toFloat()))
    }

    @Test
    fun `2 pi radians is 360 radians`() {
        assertThat(degrees(360f).toRadians()).isEqualTo(radians(2f * Math.PI.toFloat()))
    }

    @Test
    fun `180 degrees is pi radians`() {
        assertThat(degrees(180f).toRadians()).isEqualTo(radians(Math.PI.toFloat()))
    }

    @Test
    fun `pi degrees is 180 degrees`() {
        assertThat(radians(Math.PI.toFloat()).toDegrees()).isEqualTo(degrees(180f))
    }

    private fun degrees(value: Float): Degrees = value
    private fun radians(value: Float): Radians = value

}