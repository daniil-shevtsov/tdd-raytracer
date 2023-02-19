package grid

import canvas.Canvas
import canvas.applyToEveryPixel
import canvas.canvas
import canvas.color.color
import fallingsand.CellType
import fallingsand.FallingSandCell
import fallingsand.Position
import fallingsand.position
import java.lang.Integer.max
import java.lang.Integer.min

class Grid<T> private constructor(
    val width: Int,
    val height: Int,
    default: (row: Int, column: Int) -> T,
) {
    val minSize: Int
        get() = min(width, height)
    val maxSize: Int
        get() = max(width, height)

    val indices: List<List<Int>>
        get() = (0 until height).map { row ->
            (0 until width).toList()
        }
    val positions: List<Position>
        get() = (0 until height).map { row ->
            (0 until width).map { column ->
                position(row = row, column = column)
            }
        }.flatten()

    private
    val gridArray: MutableList<MutableList<T>> = (0 until height).map { row ->
        (0 until width).map { column ->
            default(row, column)
        }.toMutableList()
    }.toMutableList()

    operator fun get(row: Int, column: Int): T {
        return gridArray[row][column]
    }

    operator fun get(position: Position): T {
        return get(row = position.row, column = position.column)
    }

    fun update(transform: (row: Int, column: Int, value: T) -> T): Grid<T> {
        return Grid.createInitialized(width = width, height = height) { row, column ->
            transform(row, column, get(row, column))
        }
    }

    fun getAsLists() = gridArray.toList().map { it.toList() }

    operator fun set(row: Int, column: Int, value: T) {
        gridArray[row][column]
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

fun Grid<FallingSandCell>.toCanvas(): Canvas {
    return canvas(width = width, height = height).applyToEveryPixel { x, y ->
        when (get(y, x).type) {
            CellType.Sand -> color(0, 0, 0)
            CellType.Air -> color(1, 1, 1)
            CellType.Water -> color(0, 0, 1)
        }
    }
}