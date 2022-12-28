package ray

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import tuple.vector

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

    @Test
    fun `should give 0 for same vector`() {
        assertThat(vector(1,2,3).angleBetween(vector(1,2,3)))
            .isEqualTo(degrees(0.0).toRadians())
    }

    @Test
    fun `should give 180 for opposite vectors`() {
        assertThat(vector(1,0,0).angleBetween(vector(-1,0,0)))
            .isEqualTo(degrees(180.0).toRadians())
    }

    @Test
    fun `should give 90 for orthogonal vectors`() {
        assertThat(vector(1,0,0).angleBetween(vector(0,1,0)))
            .isEqualTo(degrees(90.0).toRadians())
    }
    
}