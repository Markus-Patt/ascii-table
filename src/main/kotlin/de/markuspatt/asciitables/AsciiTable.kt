package de.markuspatt.asciitables

import java.io.StringWriter
import java.util.*
import kotlin.math.max

internal typealias RowData = List<Any?>


class AsciiTable internal constructor(
    private val columns: List<TableColumn<*>>,
) {

    private val data = ArrayList<RowData>()

    private val maxValueLengths: MutableMap<TableColumn<*>, Int> =
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

    fun renderLines(): List<String> = buildList {
        val delimiter = " "
        val lastColumnIndex = columns.lastIndex

        val cellRenderers = columns.associateWith {
            CellRenderer(
                max(it.minWidth, maxValueLengths[it]!!),
                it.maxWidth,
                it.align
            )
        }


        StringJoiner(delimiter).let { headerRow ->
            columns.forEachIndexed { columnIndex, tableColumn ->
                val value = tableColumn.header
                val lastColumn = columnIndex == lastColumnIndex

                val valueToOutput = cellRenderers[tableColumn]!!.render(value)
                headerRow.add(
                    if (lastColumn) valueToOutput.trimEnd() else valueToOutput
                )
            }

            add(headerRow.toString())
        }


        for (datum in data) {
            val row = StringJoiner(delimiter)
            columns.forEachIndexed { columnIndex, tableColumn ->
                val value = tableColumn.formatValue(datum)
                val lastColumn = columnIndex == lastColumnIndex

                val valueToOutput = cellRenderers[tableColumn]!!.render(value)
                row.add(
                    if (lastColumn) valueToOutput.trimEnd() else valueToOutput
                )
            }
            add(row.toString())
        }

    }

}

fun asciiTable(init: AsciiTableBuilder.() -> Unit): AsciiTable = AsciiTableBuilder().apply(init).build()
