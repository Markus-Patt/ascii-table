package de.markuspatt.asciitables

import java.util.*
import kotlin.math.max
import kotlin.math.min

internal class CellRenderer(
    val minWidth: Int,
    val maxWidth: Int,
    val align: Align,
) {

    fun renderValue(value: String): String {
        val valueToRender = value.substring(0, min(value.length, maxWidth))
        val paddingLength = max(minWidth - value.length, 0)

        val fullWidthPadding = " ".repeat(paddingLength)
        val halfWidthPadding = " ".repeat(paddingLength / 2)
        val extraPadding = if (paddingLength % 2 == 1) " " else ""
        val halfWidthExtraPadding = halfWidthPadding + extraPadding


        val (prefix, suffix) = when (align) {
            Align.LEFT -> "" to fullWidthPadding

            Align.CENTER_LEFT -> halfWidthPadding to halfWidthExtraPadding
            Align.CENTER_RIGHT -> halfWidthExtraPadding to halfWidthPadding

            Align.RIGHT -> fullWidthPadding to ""
        }

        return prefix + valueToRender + suffix
    }

}

internal class AsciiTableRenderer(
    private val table: AsciiTable,
) {

    private val border = table.border

    private val columns = table.columns

    private val delimiter = border.vertical?.toString() ?: " "

    private val cellRenderers = table.columns.associateWith {
        CellRenderer(
            max(it.minWidth, table.maxValueLengths[it]!!),
            it.maxWidth,
            it.align
        )
    }

    private val result = mutableListOf<String>()

    fun renderLines(): List<String> {
        if (border.horizontal != null) {
            renderHorizontalBorderRow()
        }

        result.add(
            renderValues { it.header }
        )

        if (border.horizontal != null) {
            renderHorizontalBorderRow()
        }

        for (datum in table.data) {
            result.add(
                renderValues { it.formatValue(datum) }
            )

            if (border.horizontal != null) {
                renderHorizontalBorderRow()
            }
        }


        return result
    }

    private fun renderHorizontalBorderRow() {
        val edge = border.edge?.toString() ?: border.horizontal?.toString() ?: error("No horizontal border")

        val stringJoiner = if (border.vertical != null)
            StringJoiner(edge, edge, edge)
        else
            StringJoiner(edge)

        columns.forEach { tableColumn ->
            stringJoiner.add(
                tableColumn.renderer
                    .renderValue(" ")
                    .replace(' ', border.horizontal!!)
            )
        }

        result.add(stringJoiner.toString())

    }

    private fun renderValues(formatValue: (TableColumn<*>) -> String): String {
        return StringJoiner(
            delimiter,
            border.vertical?.toString() ?: "",
            border.vertical?.toString() ?: ""
        ).let { row ->
            columns
                .map { it to it.renderer }
                .map { (tableColumn, renderer) -> formatValue(tableColumn) to renderer }
                .map { (value, renderer) -> renderer.renderValue(value) }
                .forEach(row::add)

            row.toString().trimEnd()
        }
    }


    private val TableColumn<*>.renderer: CellRenderer
        get() = cellRenderers[this] ?: error("No cell renderer for column $this")

}
