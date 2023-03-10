package transformation

import matrix.Matrix
import matrix.identityMatrix
import matrix.matrix
import kotlin.math.cos
import kotlin.math.sin

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

fun scaling(x: Double, y: Double, z: Double): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when (column) {
                    row -> when (column) {
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

fun rotationX(angle: Double): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when {
                    row == 1 && column == 1 || row == 2 && column == 2 -> cos(angle)
                    row == 2 && column == 1 -> sin(angle)
                    row == 1 && column == 2 -> -sin(angle)
                    else -> matrix[row][column]
                }
            }
        }
    )
}

fun rotationY(angle: Double): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when {
                    row == 0 && column == 0 || row == 2 && column == 2 -> cos(angle)
                    row == 2 && column == 0 -> -sin(angle)
                    row == 0 && column == 2 -> sin(angle)
                    else -> matrix[row][column]
                }
            }
        }
    )
}

fun rotationZ(angle: Double): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when {
                    row == 0 && column == 0 || row == 1 && column == 1 -> cos(angle)
                    row == 0 && column == 1 -> -sin(angle)
                    row == 1 && column == 0 -> sin(angle)
                    else -> matrix[row][column]
                }
            }
        }
    )
}

fun shearing(
    xy: Double,
    xz: Double,
    yx: Double,
    yz: Double,
    zx: Double,
    zy: Double
): Matrix = identityMatrix(size = 4).let { matrix ->
    matrix(
        (0 until matrix.rowCount).map { row ->
            (0 until matrix.columnCount).map { column ->
                when {
                    row == 0 && column == 1 -> xy
                    row == 0 && column == 2 -> xz
                    row == 1 && column == 0 -> yx
                    row == 1 && column == 2 -> yz
                    row == 2 && column == 0 -> zx
                    row == 2 && column == 1 -> zy
                    else -> matrix[row][column]
                }
            }
        }
    )
}