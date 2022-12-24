package matrix

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class MatrixTest {

    @Test
    fun `should construct 4x4 matrix`() {
        val matrix = Matrix(
            listOf(
                listOf(1.0, 2.0, 3.0, 4.0),
                listOf(5.5, 6.5, 7.5, 8.5),
                listOf(9.0, 10.0, 11.0, 12.0),
                listOf(13.5, 14.5, 15.5, 16.5)
            )
        )
        assertThat(matrix[0, 0]).isEqualTo(1.0)
        assertThat(matrix[0, 3]).isEqualTo(4.0)
        assertThat(matrix[1, 0]).isEqualTo(5.5)
        assertThat(matrix[1, 2]).isEqualTo(7.5)
        assertThat(matrix[2, 2]).isEqualTo(11.0)
        assertThat(matrix[3, 0]).isEqualTo(13.5)
        assertThat(matrix[3, 2]).isEqualTo(15.5)
    }

}