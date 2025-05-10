package de.markuspatt.asciitables

import de.markuspatt.asciitables.builders.AsciiTableBuilder
import java.io.StringWriter

internal typealias RowData = List<Any?>


class AsciiTable internal constructor(
    internal val columns: List<TableColumn<*>>,
    internal val border: TableBorder,
) {

    internal val data = ArrayList<RowData>()

    internal val maxValueLengths: MutableMap<TableColumn<*>, Int> =
        columns.associateWith { it.header.length }.toMutableMap()

    fun add(vararg values: Any?) {
        add(values.toList())
    }

    fun add(values: List<Any?>) {
        validate(values)

        data.add(values)

        for (i in values.indices) {
            val tableColumn = columns[i]
            updateMaxValueLengths(tableColumn, tableColumn.getRequestedWidth(values))
        }
    }

    private fun updateMaxValueLengths(tableColumn: TableColumn<*>, value: Int) {
        if (maxValueLengths[tableColumn]!! < value) {
            maxValueLengths[tableColumn] = value
        }
    }

    private fun validate(values: RowData) {
        require(columns.size == values.size) {
            "wrong number of arguments: expected ${columns.size}, got ${values.size}"
        }

        for (i in values.indices) {
            val tableColumn = columns[i]

            require(tableColumn.validateValue(values[i])) {
                "invalid argument at index $i: ${values[i]} (${values[i]!!.javaClass}) for $tableColumn"
            }
        }

    }

    fun printToString(): String = StringWriter().use { stringWriter ->
        printTo(stringWriter)
        stringWriter.flush()
        return stringWriter.toString()
    }

    fun printTo(target: Appendable) {
        renderLines().forEach { target.append("$it\n") }
    }

    fun renderLines(): List<String> = AsciiTableRenderer(this@AsciiTable).renderLines()

}

fun asciiTable(init: AsciiTableBuilder.() -> Unit): AsciiTable = AsciiTableBuilder().apply(init).build()
