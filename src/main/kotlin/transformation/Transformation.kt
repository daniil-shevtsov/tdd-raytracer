package transformation

import matrix.Matrix
import matrix.identityMatrix
import matrix.matrix

fun translation(x: Double, y: Double, z: Double): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when (column) {
                    matrix.columnCount - 1 -> when (row) {
                        0 -> x
                        1 -> y
                        2 -> z
                        else -> matrix[row][column]
                    }
                    else -> matrix[row][column]
                }
            }
        }
    )
}