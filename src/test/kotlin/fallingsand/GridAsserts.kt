package fallingsand

import assertk.Assert
import assertk.all
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import assertk.assertions.support.expected
import grid.Grid

fun Assert<FallingSandCell>.hasPosition(row: Int, column: Int) {
    prop(FallingSandCell::position).all {
        prop(Position::column).isEqualTo(column)
        prop(Position::row).isEqualTo(row)
    }
}

//    private fun Assert<ChangeCandidate>.hasPosition(row: Int, column: Int) {
//        isInstanceOf(ChangeCandidate.Change::class)
//        .prop(ChangeCandidate.Change::destinationPosition).all {
//            prop(Position::column).isEqualTo(column)
//            prop(Position::row).isEqualTo(row)
//        }
//    }

fun Assert<FallingSandCell>.hasType(type: CellType) {
    prop(FallingSandCell::type).isEqualTo(type)
}

fun Assert<Grid<FallingSandCell>>.hasTypes(
    vararg expected: Pair<Position, CellType>,
) = given { actual ->
    val actualTypes = actual.getAsLists().flatMap { row ->
        row.map { cell ->
            cell.position to cell.type
        }
    }.toMap()
    val expectedTypes = expected.toMap()

    val difference = actualTypes.filter { expectedTypes[it.key] != null && expectedTypes[it.key] != it.value }

    if (difference.isEmpty()) {
        return@given
    }

    fun CellType.char() = when (this) {
        CellType.Air -> "A"
        CellType.Sand -> "S"
        CellType.Water -> "W"
    }

    fun Collection<Position>.indexWindow() = IndexWindow(
        rowMin = minOf { it.row },
        rowMax = maxOf { it.row },
        columnMin = minOf { it.column },
        columnMax = maxOf { it.column },
    )

    fun Map<Position, CellType>.picture(
        indexWindow: IndexWindow = keys.indexWindow()
    ) =
        toList().filter { (position, _) ->
            position.column in IntRange(indexWindow.columnMin, indexWindow.columnMax)
                    && position.row in IntRange(indexWindow.rowMin, indexWindow.rowMax)
        }.joinToString(separator = "") { (position, type) ->
            type.char() + when (position.column) {
                maxBy { it.key.column }.key.column -> "\n"
                else -> ""
            }
        }

    val expectedWindow = expectedTypes.keys.indexWindow()

    val actualPicture = actualTypes.picture(expectedWindow)
    val expectedPicture = expectedTypes.picture(expectedWindow)

    val expectedString = expectedTypes.toList().filter { difference.containsKey(it.first) }.joinToString(
        prefix = "{",
        postfix = "}",
        separator = "\n"
    ) { "[${it.first.row}:${it.first.column}] = ${it.second.name}" }

    val differenceString = difference.toList().joinToString(
        prefix = "{",
        postfix = "}",
        separator = "\n"
    ) { "[${it.first.row}:${it.first.column}] = ${it.second.name}" }

    expected(
        message = ":<$expectedString> but was:<$differenceString>\nexpected grid:\n$expectedPicture\nbut was\n$actualPicture",
        expected = expectedTypes,
        actual = actualTypes
    )
}

private data class IndexWindow(
    val rowMin: Int,
    val rowMax: Int,
    val columnMin: Int,
    val columnMax: Int,
)