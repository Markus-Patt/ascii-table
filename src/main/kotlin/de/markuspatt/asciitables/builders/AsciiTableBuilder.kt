package de.markuspatt.asciitables.builders

import de.markuspatt.asciitables.AsciiTable
import de.markuspatt.asciitables.NoBorder
import de.markuspatt.asciitables.TableBorder
import de.markuspatt.asciitables.TableColumn
import java.text.NumberFormat

class AsciiTableBuilder internal constructor() {

    private val tableColumns: MutableList<TableColumn<*>> = ArrayList()

    private var border: TableBorder = NoBorder

    fun build(): AsciiTable {
        check(tableColumns.isNotEmpty()) {
            "unable to build AsciiTable without columns"
        }

        return AsciiTable(
            tableColumns,
            border
        )
    }

    fun stringColumn(header: String, configure: StringColumnBuilder.() -> Unit = {}): AsciiTableBuilder {
        val columnsBuilder = StringColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header

        configure(columnsBuilder)

        addColumn(columnsBuilder.build())

        return this
    }

    fun longColumn(header: String, configure: LongColumnBuilder.() -> Unit = {}): AsciiTableBuilder {
        val columnsBuilder = LongColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header

        configure(columnsBuilder)

        addColumn(columnsBuilder.build())

        return this
    }

    fun doubleColumn(
        header: String,
        configure: DoubleColumnBuilder.() -> Unit = {},
    ): AsciiTableBuilder {
        val columnsBuilder = DoubleColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header

        configure(columnsBuilder)

        addColumn(columnsBuilder.build())

        return this
    }

    fun percentColumn(
        header: String,
        configure: DoubleColumnBuilder.() -> Unit = {},
    ): AsciiTableBuilder {
        val columnsBuilder = DoubleColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header

        configure(columnsBuilder)

        columnsBuilder.numberFormat = NumberFormat.getPercentInstance().apply {
            maximumFractionDigits = columnsBuilder.precision
        }


        addColumn(columnsBuilder.build())

        return this
    }

    fun progressBarColumn(
        header: String,
        configure: ProgressBarColumnBuilder.() -> Unit = {},
    ): AsciiTableBuilder {
        val columnsBuilder = ProgressBarColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header

        configure(columnsBuilder)

        addColumn(columnsBuilder.build())

        return this
    }

    private fun addColumn(field: TableColumn<*>) {
        tableColumns.add(field)
    }

    fun withoutBorder() {
        border = NoBorder
    }

    fun withBorder(configuration: BorderBuilder.() -> Unit = {}) {
        val borderBuilder = BorderBuilderImpl()

        configuration(borderBuilder)

        border = borderBuilder.build()
    }

}
