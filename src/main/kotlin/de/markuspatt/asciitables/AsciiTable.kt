package de.markuspatt.asciitables

import java.io.StringWriter
import java.util.*

internal typealias RowData = List<Any?>


class AsciiTable internal constructor(
    private val columns: List<TableColumn<*>>,
) {

    private val data = ArrayList<RowData>()

    private val maxValueLengths: IntArray = IntArray(columns.size).apply {
        for (i in indices) {
            this[i] = columns[i].header.length
        }
    }

    fun add(vararg values: Any?) {
        add(values.toList())
    }

    fun add(values: List<Any?>) {
        validate(values)

        data.add(values)

        for (i in values.indices) {
            val tableColumn = columns[i]
            updateMaxValueLengths(i, tableColumn.getRequestedWidth(values))
        }
    }

    private fun updateMaxValueLengths(index: Int, value: Int) {
        if (maxValueLengths[index] < value) {
            maxValueLengths[index] = value
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

    fun printToString(): String {
        StringWriter().use { stringWriter ->
            printTo(stringWriter)
            stringWriter.flush()
            return stringWriter.toString()
        }
    }

    fun printTo(target: Appendable) {
        renderLines().forEach { target.append("$it\n") }
    }

    fun renderLines(): List<String> {
        return buildList<String> {
            val delimiter = " "
            val headerRow = StringJoiner(delimiter)

            val lastIndex = columns.lastIndex

            columns.forEachIndexed { index, tableColumn ->
                val value = tableColumn.header
                val width = if (index == lastIndex)
                    value.length
                else
                    maxValueLengths[tableColumn.index]

                headerRow.add(
                    tableColumn.pad(
                        value,
                        width
                    )
                )
            }

            add(headerRow.toString())

            for (datum in data) {
                val row = StringJoiner(delimiter)
                columns.forEachIndexed { index, tableColumn ->
                    val value = tableColumn.formatValue(datum)
                    val width = if (index == lastIndex)
                        value.length
                    else
                        maxValueLengths[tableColumn.index]

                    row.add(
                        tableColumn.pad(
                            value,
                            width
                        )
                    )
                }
                add(row.toString())
            }

        }
    }

}

fun asciiTable(init: AsciiTableBuilder.() -> Unit): AsciiTable {
    return AsciiTableBuilder().apply(init).build()
}
