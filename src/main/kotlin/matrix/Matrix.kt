package matrix

import kotlin.math.abs

class Matrix(
    val values: List<List<Double>>,
) {

    val size: Int
        get() = values.size
    val rowCount: Int
        get() = values.size
    val columnCount: Int
        get() = values[0].size

    operator fun get(row: Int, column: Int): Double {
        return values[row][column]
    }

    operator fun get(row: Int): List<Double> {
        return values[row]
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Matrix -> values.size == other.values.size &&
                    values.indices.all { row ->
                        values.indices.all { column ->
                            abs(values[row][column] - other.values[row][column]) < EPSILON
                        }
                    }
            else -> super.equals(other)
        }
    }

    operator fun times(other: Matrix): Matrix {
        val a = this
        val b = other

        val aRows = a.values.indices
        val bRows = a.values.indices
        val rowsA = a.size
        val colsA = a[0].size
        val rowsB = b.size
        val colsB = b[0].size
        if (colsA != rowsB) {
            throw IllegalArgumentException("Incompatible matrices for multiplication")
        }
        val result = (0 until a.rowCount).map { i ->
            (0 until b.rowCount).map { j ->
                (0 until a.columnCount).sumOf { k -> a[i][k] * b[k][j] }
            }
        }
        return Matrix(result)
    }

    override fun toString(): String {
        return values.mapIndexed { column, row ->
            row.joinToString(separator = " ") { value ->
                value.toString()
            }
        }.joinToString(separator = "\n")
    }

    private companion object {
        const val EPSILON = 0.00001
    }
}

fun matrix(values: List<List<Double>>) = Matrix(values = values)