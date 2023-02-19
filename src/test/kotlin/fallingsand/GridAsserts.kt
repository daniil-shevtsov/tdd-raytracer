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

fun Assert<FallingSandSimulationState>.hasTypess(
    vararg expected: Pair<Position, CellType>,
) = prop(FallingSandSimulationState::grid).hasTypes(*expected)

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
        message = ":<$expectedString> but was:<$differenceString>",
        expected = expectedTypes,
        actual = actualTypes
    )
}