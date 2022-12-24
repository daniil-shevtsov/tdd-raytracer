package matrix

class Matrix(
    val values: List<List<Double>>,
) {
    operator fun get(row: Int, column: Int): Double {
        return values[row][column]
    }
}