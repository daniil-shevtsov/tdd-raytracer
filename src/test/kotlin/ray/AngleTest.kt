package ray

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class AngleTest {

    @Test
    fun `0 degrees is 0 radians`() {
        assertThat(degrees(0.0).toRadians()).isEqualTo(radians(0.0))
    }

    @Test
    fun `0 radians is 0 degrees`() {
        assertThat(radians(0.0).toDegrees()).isEqualTo(degrees(0.0))
    }

    @Test
    fun `360 degrees is 2 pi radians`() {
        assertThat(degrees(360.0).toRadians()).isEqualTo(radians(2f * Math.PI))
    }

    @Test
    fun `2 pi radians is 360 radians`() {
        assertThat(degrees(360.0).toRadians()).isEqualTo(radians(2f * Math.PI))
    }

    @Test
    fun `180 degrees is pi radians`() {
        assertThat(degrees(180.0).toRadians()).isEqualTo(radians(Math.PI))
    }

    @Test
    fun `pi degrees is 180 degrees`() {
        assertThat(radians(Math.PI).toDegrees()).isEqualTo(degrees(180.0))
    }

}