package de.markuspatt.asciitables.builders

import de.markuspatt.asciitables.Align
import de.markuspatt.asciitables.NumberColumn
import de.markuspatt.asciitables.StringColumn
import de.markuspatt.asciitables.TableColumn
import java.text.NumberFormat

interface ColumnBuilder {

    val columnIndex: Int

    var header: String

}

interface CommonColumnBuilder : ColumnBuilder {

    var align: Align

    var minWidth: Int

}

interface StringColumnBuilder : CommonColumnBuilder {

    var maxWidth: Int
}

interface NumberColumnBuilder : CommonColumnBuilder

interface LongColumnBuilder : NumberColumnBuilder

interface DoubleColumnBuilder : NumberColumnBuilder {

    var precision: Int

}

interface ProgressBarColumnBuilder : ColumnBuilder {

    var width: Int

    var barChar: Char

}


internal abstract class ColumnBuilderImpl<T : TableColumn<*>>(
    override val columnIndex: Int,
) : ColumnBuilder {

    override var header: String = ""

    internal abstract fun build(): T

}

internal abstract class CommonColumnBuilderImpl<T : TableColumn<*>>(
    columnIndex: Int,
) : ColumnBuilderImpl<T>(columnIndex), CommonColumnBuilder {

    override var align: Align = Align.LEFT

    override var minWidth: Int = 0

}

internal class StringColumnBuilderImpl(columnIndex: Int) : CommonColumnBuilderImpl<StringColumn>(columnIndex),
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

internal abstract class NumberColumnBuilderImpl(columnIndex: Int) : CommonColumnBuilderImpl<NumberColumn>(columnIndex),
    NumberColumnBuilder {

    open var precision: Int = 2

    open var numberFormat: NumberFormat? = null

    init {
        align = Align.RIGHT
    }


    override fun build(): NumberColumn {
        return NumberColumn(
            columnIndex,
            header,
            align,
            minWidth,
            Int.MAX_VALUE,
            precision,
            numberFormat ?: NumberColumn.defaultNumberFormat(precision),
        )
    }

}

internal class LongColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), LongColumnBuilder

internal class DoubleColumnBuilderImpl(columnIndex: Int) : NumberColumnBuilderImpl(columnIndex), DoubleColumnBuilder {

    override var precision: Int = 2

}

internal class ProgressBarColumnBuilderImpl(columnIndex: Int) :
    ColumnBuilderImpl<StringColumn>(columnIndex),
    ProgressBarColumnBuilder {

    override var width: Int = 10

    override var barChar: Char = '='

    override fun build(): StringColumn {
        return StringColumn(columnIndex, header, Align.LEFT, width, width) {
            if (it == null) return@StringColumn null
            check(it is Number) { "progress bar column only accepts numbers, got $it" }

            val percent = it.toDouble()/* / 100*/
            val filled = (percent * width).toInt()
            val remaining = width - filled
            val progress = barChar.toString().repeat(filled) + " ".repeat(remaining)
            progress
        }
    }

}
