package de.markuspatt.asciitables.builders

import de.markuspatt.asciitables.Align
import de.markuspatt.asciitables.NumberColumn
import de.markuspatt.asciitables.StringColumn
import de.markuspatt.asciitables.TableColumn

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
