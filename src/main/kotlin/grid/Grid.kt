package grid

import java.lang.Integer.max
import java.lang.Integer.min

class Grid<T> private constructor(
    private val width: Int,
    private val height: Int,
    default: (row: Int, column: Int) -> T,
) {
    val minSize: Int
        get() = min(width, height)
    val maxSize: Int
        get() = max(width, height)

    private val gridArray: MutableList<MutableList<T>> = (0 until height).map { row ->
        (0 until width).map { column ->
            default(row, column)
        }.toMutableList()
    }.toMutableList()

    operator fun get(row: Int, column: Int): T {
        return gridArray[row][column]
    }

    fun update(row: Int, column: Int, value: T) {
        gridArray[row][column] = value
    }

    companion object {
        fun <T> createInitialized(width: Int, height: Int, init: (row: Int, column: Int) -> T): Grid<T> {
            return Grid(width, height, init)
        }

        fun <T> createInitialized(size: Int, init: (row: Int, column: Int) -> T): Grid<T> {
            return createInitialized(size, size, init)
        }
    }
}