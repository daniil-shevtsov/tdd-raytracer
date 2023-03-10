package matrix

import assertk.assertThat
import assertk.assertions.*
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

    @Test
    fun `should calculate determinant of 2x2`() {
        val matrix = matrix(
            listOf(
                row(1.0, 5.0),
                row(-3.0, 2.0),
            )
        )
        assertThat(matrix.determinant())
            .isEqualTo(17.0)
    }

    @Test
    fun `submatrix of a 3x3 matrix gives 2x2 matrix`() {
        val matrix = matrix(
            listOf(
                row(1.0, 5.0, 0.0),
                row(-3.0, 2.0, 7.0),
                row(0.0, 6.0, -3.0),
            )
        )
        assertThat(matrix.submatrix(0, 2))
            .isEqualTo(
                matrix(
                    listOf(
                        row(-3.0, 2.0),
                        row(0.0, 6.0),
                    )
                )
            )
    }

    @Test
    fun `submatrix of 4x4 is 3x3`() {
        val matrix = matrix(
            listOf(
                row(-6.0, 1.0, 1.0, 6.0),
                row(-8.0, 5.0, 8.0, 6.0),
                row(-1.0, 0.0, 8.0, 2.0),
                row(-7.0, 1.0, -1.0, 1.0),
            )
        )

        assertThat(matrix.submatrix(2, 1))
            .isEqualTo(
                matrix(
                    listOf(
                        row(-6.0, 1.0, 6.0),
                        row(-8.0, 8.0, 6.0),
                        row(-7.0, -1.0, 1.0),
                    )
                )
            )
    }

    @Test
    fun `should calculate minor of a 3x3 matrix`() {
        val matrix = matrix(
            listOf(
                row(3.0, 5.0, 0.0),
                row(2.0, -1.0, -7.0),
                row(6.0, -1.0, 5.0),
            )
        )
        assertThat(matrix.submatrix(1, 0).determinant())
            .isEqualTo(25.0)
        assertThat(matrix.minor(1, 0)).isEqualTo(25.0)
    }

    @Test
    fun `should calculate cofactor`() {
        val matrix = matrix(
            listOf(
                row(3.0, 5.0, 0.0),
                row(2.0, -1.0, -7.0),
                row(6.0, -1.0, 5.0),
            )
        )

        assertThat(matrix.minor(0, 0)).isEqualTo(-12.0)
        assertThat(matrix.cofactor(0, 0)).isEqualTo(-12.0)
        assertThat(matrix.minor(1, 0)).isEqualTo(25.0)
        assertThat(matrix.cofactor(1, 0)).isEqualTo(-25.0)
    }

    @Test
    fun `should calculate determinant of a 3x3 matrix`() {
        val matrix = matrix(
            listOf(
                row(1.0, 2.0, 6.0),
                row(-5.0, 8.0, -4.0),
                row(2.0, 6.0, 4.0),
            )
        )
        assertThat(matrix.cofactor(0, 0)).isEqualTo(56.0)
        assertThat(matrix.cofactor(0, 1)).isEqualTo(12.0)
        assertThat(matrix.cofactor(0, 2)).isEqualTo(-46.0)
        assertThat(matrix.determinant()).isEqualTo(-196.0)
    }

    @Test
    fun `should calculate the determinant of a 4x4 matrix`() {
        val matrix = matrix(
            listOf(
                row(-2.0, -8.0, 3.0, 5.0),
                row(-3.0, 1.0, 7.0, 3.0),
                row(1.0, 2.0, -9.0, 6.0),
                row(-6.0, 7.0, 7.0, -9.0),
            )
        )
        assertThat(matrix.cofactor(0, 0)).isEqualTo(690.0)
        assertThat(matrix.cofactor(0, 1)).isEqualTo(447.0)
        assertThat(matrix.cofactor(0, 2)).isEqualTo(210.0)
        assertThat(matrix.cofactor(0, 3)).isEqualTo(51.0)
        assertThat(matrix.determinant()).isEqualTo(-4071.0)
    }

    @Test
    fun `should indicate invertibility of an invertible matrix`() {
        val matrix = matrix(
            listOf(
                row(6.0, 4.0, 4.0, 4.0),
                row(5.0, 5.0, 7.0, 6.0),
                row(4.0, -9.0, 3.0, -7.0),
                row(9.0, 1.0, 7.0, -6.0),
            )
        )
        assertThat(matrix.determinant()).isEqualTo(-2120.0)
        assertThat(matrix.isInvertible).isTrue()
    }

    @Test
    fun `should indicate non-invertibility of an non-invertible matrix`() {
        val matrix = matrix(
            listOf(
                row(-4.0, 2.0, -2.0, -3.0),
                row(9.0, 6.0, 2.0, 6.0),
                row(0.0, -5.0, 1.0, -5.0),
                row(0.0, 0.0, 0.0, 0.0),
            )
        )
        assertThat(matrix.determinant()).isEqualTo(0.0)
        assertThat(matrix.isInvertible).isFalse()
    }

    @Test
    fun `should calculate inverse of a matrix`() {
        val matrix = matrix(
            listOf(
                row(-5.0, 2.0, 6.0, -8.0),
                row(1.0, -5.0, 1.0, 8.0),
                row(7.0, 7.0, -6.0, -7.0),
                row(1.0, -3.0, 7.0, 4.0),
            )
        )
        val inversed = matrix.inversedOrNull()
        assertThat(inversed).isNotNull()
        inversed!!
        assertThat(matrix.determinant()).isEqualTo(532.0)
        assertThat(matrix.cofactor(2, 3)).isEqualTo(-160.0)
        assertThat(inversed[3][2]).isEqualTo(-160.0 / 532.0)
        assertThat(matrix.cofactor(3, 2)).isEqualTo(105.0)
        assertThat(inversed[2][3]).isEqualTo(105.0 / 532.0)
        assertThat(inversed).isEqualTo(
            matrix(
                listOf(
                    row(0.21805, 0.45113, 0.24060, -0.04511),
                    row(-0.80827, -1.45677, -0.44361, 0.52068),
                    row(-0.07895, -0.22368, -0.05263, 0.19737),
                    row(-0.52256, -0.81391, -0.30075, 0.30639),
                )
            )
        )
    }

    @Test
    fun `calculate unverse of another matrix`() {
        val matrix = matrix(
            listOf(
                row(8.0, -5.0, 9.0, 2.0),
                row(7.0, 5.0, 6.0, 1.0),
                row(-6.0, 0.0, 9.0, 6.0),
                row(-3.0, 0.0, -9.0, -4.0),
            )
        )

        assertThat(matrix.inversedOrNull())
            .isEqualTo(
                matrix(
                    listOf(
                        row(-0.15385, -0.15385, -0.28205, -0.53846),
                        row(-0.07692, 0.12308, 0.02564, 0.03077),
                        row(0.35897, 0.35897, 0.43590, 0.92308),
                        row(-0.69231, -0.69231, -0.76923, -1.92308),
                    )
                )
            )
    }

    fun `calculate inverse of a third matrix`() {
        val matrix = matrix(
            listOf(
                row(9.0, 3.0, 0.0, 9.0),
                row(-5.0, -2.0, -6.0, -3.0),
                row(-4.0, 9.0, 6.0, 4.0),
                row(-7.0, 6.0, 6.0, 2.0),
            )
        )

        assertThat(matrix.inversedOrNull())
            .isEqualTo(
                matrix(
                    listOf(
                        row(-0.04074, -0.07778, 0.14444, -0.22222),
                        row(-0.07778, 0.03333, 0.36667, -0.33333),
                        row(-0.02901, -0.14630, -0.10926, 0.12963),
                        row(0.17778, 0.06667, -0.26667, 0.33333),
                    )
                )
            )
    }

    fun `should get orignial matrix when multiplying product by its inverse`() {
        val a = matrix(
            listOf(
                row(3.0, -9.0, 7.0, 3.0),
                row(3.0, -8.0, 2.0, -9.0),
                row(-4.0, 4.0, 4.0, 1.0),
                row(-6.0, 5.0, -1.0, 1.0),
            )
        )
        val b = matrix(
            listOf(
                row(8.0, 2.0, 2.0, 2.0),
                row(3.0, -1.0, 7.0, 0.0),
                row(7.0, 0.0, 5.0, 4.0),
                row(6.0, -2.0, 0.0, 5.0),
            )
        )
        val c = a * b

        assertThat(c * b.inversedOrNull()!!).isEqualTo(a)
    }
}