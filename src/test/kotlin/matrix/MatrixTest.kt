package matrix

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import org.junit.jupiter.api.Test
import tuple.Tuple
import tuple.point

internal class MatrixTest {

    @Test
    fun `should construct 4x4 matrix`() {
        val matrix = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
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

    @Test
    fun `should create 2x2 matrix`() {
        val matrix = matrix(
            listOf(
                row(-3.0, 5.0),
                row(1.0, -2.0),
            )
        )
        assertThat(matrix[0, 0]).isEqualTo(-3.0)
        assertThat(matrix[0, 1]).isEqualTo(5.0)
        assertThat(matrix[1, 0]).isEqualTo(1.0)
        assertThat(matrix[1, 1]).isEqualTo(-2.0)
    }

    @Test
    fun `should create 3x3 matrix`() {
        val matrix = matrix(
            listOf(
                row(-3.0, 5.0, 0.0),
                row(1.0, -2.0, -7.0),
                row(0.0, 1.0, 1.0),
            )
        )
        assertThat(matrix[0, 0]).isEqualTo(-3.0)
        assertThat(matrix[1, 1]).isEqualTo(-2.0)
        assertThat(matrix[2, 2]).isEqualTo(1.0)
    }

    @Test
    fun `should compare equal matrices`() {
        val a = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )
        val b = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )

        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should ignore epsilon when comparing matrices`() {
        val a = matrix(
            listOf(
                row(1.00001, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )
        val b = matrix(
            listOf(
                row(1.00002, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )

        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `should consider difference more than epsilon when comparing matrices`() {
        val a = matrix(
            listOf(
                row(1.0001, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )
        val b = matrix(
            listOf(
                row(1.0002, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )

        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `should compare not equal matrices`() {
        val a = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(5.5, 6.5, 7.5, 8.5),
                row(9.0, 10.0, 11.0, 12.0),
                row(13.5, 14.5, 15.5, 16.5)
            )
        )
        val b = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(4.5, 6.5, 7.5, 8.5),
                row(9.0, 9.0, 11.0, 12.0),
                row(13.5, 14.5, 11.5, 16.5)
            )
        )

        assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `should multiply matrices`() {
        val a = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(5.0, 6.0, 7.0, 8.0),
                row(9.0, 8.0, 7.0, 6.0),
                row(5.0, 4.0, 3.0, 2.0),
            )
        )
        val b = matrix(
            listOf(
                row(-2.0, 1.0, 2.0, 3.0),
                row(3.0, 2.0, 1.0, -1.0),
                row(4.0, 3.0, 6.0, 5.0),
                row(1.0, 2.0, 7.0, 8.0),
            )
        )

        assertThat(a * b).isEqualTo(
            matrix(
                listOf(
                    row(20.0, 22.0, 50.0, 48.0),
                    row(44.0, 54.0, 114.0, 108.0),
                    row(40.0, 58.0, 110.0, 102.0),
                    row(16.0, 26.0, 46.0, 42.0),
                )
            )
        )
    }

    @Test
    fun `should multiple matrix by tuple`() {
        val a = matrix(
            listOf(
                row(1.0, 2.0, 3.0, 4.0),
                row(2.0, 4.0, 4.0, 2.0),
                row(8.0, 6.0, 4.0, 1.0),
                row(0.0, 0.0, 0.0, 1.0),
            )
        )

        val b = point(1.0, 2.0, 3.0)

        assertThat(a * b).isEqualTo(point(18.0, 24.0, 33.0))
    }

    @Test
    fun `multiplying a matrix by the identity matrix gives the same matrix`() {
        val matrix = matrix(
            listOf(
                row(0.0, 1.0, 2.0, 4.0),
                row(1.0, 2.0, 4.0, 8.0),
                row(2.0, 4.0, 8.0, 16.0),
                row(4.0, 8.0, 16.0, 32.0),
            )
        )

        assertThat(matrix * identityMatrix()).isEqualTo(matrix)
    }

    @Test
    fun `multiplying an identity matrix by the tuple gives the same tuple`() {
        val tuple = Tuple(1.0, 2.0, 3.0, 4.0)

        assertThat(identityMatrix() * tuple).isEqualTo(tuple)
    }

    @Test
    fun `should transpose matrix`() {
        val matrix = matrix(
            listOf(
                row(0.0, 9.0, 3.0, 0.0),
                row(9.0, 8.0, 0.0, 8.0),
                row(1.0, 8.0, 5.0, 3.0),
                row(0.0, 0.0, 5.0, 8.0),
            )
        )

        assertThat(matrix.transposed()).isEqualTo(
            matrix(
                listOf(
                    row(0.0, 9.0, 1.0, 0.0),
                    row(9.0, 8.0, 8.0, 0.0),
                    row(3.0, 0.0, 5.0, 5.0),
                    row(0.0, 8.0, 3.0, 8.0),
                )
            )
        )
    }

    @Test
    fun `transposing identity matrix gives identity matrix`() {
        assertThat(identityMatrix().transposed()).isEqualTo(identityMatrix())
    }

}