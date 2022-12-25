package matrix

import tuple.Tuple
import tuple.Vector
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
            is Matrix -> values.size == other.values.size && values.indices.all { row ->
                values.indices.all { column ->
                    abs(values[row][column] - other.values[row][column]) < EPSILON
                }
            }
            else -> super.equals(other)
        }
    }

    operator fun times(other: Matrix): Matrix {
        if (columnCount != other.rowCount) {
            throw IllegalArgumentException("Incompatible matrices for multiplication")
        }
        val result = (0 until rowCount).map { i ->
            (0 until other.rowCount).map { j ->
                (0 until columnCount).sumOf { k -> this[i][k] * other[k][j] }
            }
        }
        return Matrix(result)
    }

    operator fun times(vector: Vector): Vector {
        val vectorAsColumn = listOf(vector.x, vector.y, vector.z, vector.w)
        if (columnCount != vectorAsColumn.size) {
            throw IllegalArgumentException("Incompatible matrix and vector for multiplication")
        }

        val result = (vectorAsColumn.indices).map { row ->
            (0 until columnCount).sumOf { column ->
                this[row][column] * vectorAsColumn[column]
            }
        }
        return result.let { vectorMatrix ->
            Tuple(
                x = vectorMatrix[0],
                y = vectorMatrix[1],
                z = vectorMatrix[2],
                w = vectorMatrix[3],
            )
        }
    }

    fun transposed(): Matrix {
        return Matrix((0 until rowCount).map { row ->
            (0 until columnCount).map { column ->
                this[column][row]
            }
        })
    }

    //TODO: Currently only for 2x2
    fun determinant(): Double {
        return values[0][0] * values[1][1] - values[0][1] * values[1][0]
    }

    fun submatrix(withoutRow: Int, withoutColumn: Int): Matrix {
        return matrix(
            values.mapIndexedNotNull { row, rowValues ->
                when (row) {
                    withoutRow -> null
                    else -> rowValues.mapIndexedNotNull { column, value ->
                        when (column) {
                            withoutColumn -> null
                            else -> value
                        }
                    }
                }
            }
        )
    }

    fun minor(row: Int, column: Int): Double {
        return submatrix(withoutRow = row, withoutColumn = column)
            .determinant()
    }

    fun cofactor(row: Int, column: Int): Double {
        val sign = when {
            (row + column) % 2 == 0 -> 1
            else -> -1
        }
        return sign * minor(row, column)
    }

    override fun toString(): String {
        return values.mapIndexed { column, row ->
            row.joinToString(separator = " ") { value ->
                value.toString()
            }
        }.joinToString(separator = "\n")
    }

    companion object {
        private const val EPSILON = 0.00001
    }
}

fun identityMatrix(size: Int = 4) = Matrix((0 until size).map { row ->
    (0 until size).map { column ->
        when (column) {
            row -> 1.0
            else -> 0.0
        }
    }
})

fun matrix(values: List<List<Double>>) = Matrix(values = values)
fun row(vararg values: Double): List<Double> = values.toList()