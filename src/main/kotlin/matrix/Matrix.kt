package matrix

import kotlin.math.abs

class Matrix(
    val values: List<List<Double>>,
) {
    operator fun get(row: Int, column: Int): Double {
        return values[row][column]
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