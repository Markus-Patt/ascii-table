package de.markuspatt.asciitables

class AsciiTableBuilder internal constructor() {

    private val tableColumns: MutableList<TableColumn<*>> = ArrayList()

    private var border: Border = NoBorder

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
        configure: DoubleColumnBuilder.() -> Unit = {},
    ): AsciiTableBuilder {
        val columnsBuilder = DoubleColumnBuilderImpl(tableColumns.size)

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

    fun withBorder(function: () -> Unit = {}) {
        TODO("Not yet implemented")
    }

}

interface ColumnBuilder {

    val columnIndex: Int

    var header: String

    var align: Align

    var minWidth: Int

}

interface StringColumnBuilder : ColumnBuilder {

    var maxWidth: Int
}

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


    internal abstract fun build(): T

}

internal class StringColumnBuilderImpl(columnIndex: Int) : ColumnBuilderImpl<StringColumn>(columnIndex),
    StringColumnBuilder {

    override var maxWidth: Int = Integer.MAX_VALUE

    override fun build(): StringColumn {
        return StringColumn(
            columnIndex,
            header,
            align,
            minWidth,
            maxWidth
        )
    }

}

internal abstract class NumberColumnBuilderImpl(columnIndex: Int) : ColumnBuilderImpl<NumberColumn>(columnIndex),
    NumberColumnBuilder {

    open var precision: Int = 2


    override fun build(): NumberColumn {
        return NumberColumn(
            columnIndex,
            header,
            align,
            minWidth,
            Int.MAX_VALUE,
            precision
        )
    }

}

internal class LongColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), LongColumnBuilder

internal class DoubleColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), DoubleColumnBuilder {

    override var precision: Int = 2

}

internal interface Border

internal object NoBorder : Border
