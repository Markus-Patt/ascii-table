package de.markuspatt.asciitables

class AsciiTableBuilder internal constructor() {

    private val tableColumns: MutableList<TableColumn<*>> = ArrayList()

    fun build(): AsciiTable {
        check(tableColumns.isNotEmpty()) {
            "unable to build AsciiTable without columns"
        }

        return AsciiTable(tableColumns)
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
        precision: Int,
        configure: DoubleColumnBuilder.() -> Unit = {},
    ): AsciiTableBuilder {
        val columnsBuilder = DoubleColumnBuilderImpl(tableColumns.size)

        columnsBuilder.header = header
        columnsBuilder.precision = precision

        configure(columnsBuilder)

        addColumn(columnsBuilder.build())

        return this
    }

    private fun addColumn(field: TableColumn<*>) {
        tableColumns.add(field)
    }

}

interface ColumnBuilder {

    val columnIndex: Int

    var header: String

    var align: Align

    var minWidth: Int

    var maxWidth: Int

}

interface StringColumnBuilder : ColumnBuilder

interface NumberColumnBuilder : ColumnBuilder

interface LongColumnBuilder : NumberColumnBuilder

interface DoubleColumnBuilder : NumberColumnBuilder {

    var precision: Int

}


internal abstract class ColumnBuilderImpl<T : TableColumn<*>>(
    override val columnIndex: Int,
) : ColumnBuilder {

    override var header: String = ""

    override var align: Align = Align.LEFT

    override var minWidth: Int = 0

    override var maxWidth: Int = Integer.MAX_VALUE


    internal abstract fun build(): T

}

internal class StringColumnBuilderImpl(columnIndex: Int) : ColumnBuilderImpl<StringColumn>(columnIndex),
    StringColumnBuilder {

    override fun build(): StringColumn {
        return StringColumn(columnIndex, header, align, minWidth, maxWidth)
    }

}

internal abstract class NumberColumnBuilderImpl(columnIndex: Int) : ColumnBuilderImpl<NumberColumn>(columnIndex), NumberColumnBuilder {

    open var precision: Int = 2

    override fun build(): NumberColumn {
        return NumberColumn(columnIndex, header, align, minWidth, maxWidth, precision)
    }

}

internal class LongColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), LongColumnBuilder

internal class DoubleColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), DoubleColumnBuilder {

    override var precision: Int = 2

}
