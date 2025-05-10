package de.markuspatt.asciitables

import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.log10


internal abstract class TableColumn<T>(
    val index: Int,
    val header: String,
    val align: Align,
    val minWidth: Int,
    val maxWidth: Int,
) {

    fun validateValue(value: Any?): Boolean {
        return (value == null) || validateType(value)
    }

    abstract fun validateType(value: Any): Boolean

    fun getRequestedWidth(rowData: RowData): Int {
        return doGetRequestedWidth(getValueFromRow(rowData))
    }

    abstract fun doGetRequestedWidth(value: T): Int

    abstract fun doFormatValue(value: T): String

    abstract fun getValueFromRow(rowData: RowData): T

    fun formatValue(rowData: RowData): String {
        return doFormatValue(getValueFromRow(rowData) ?: return "")
    }

}

internal class StringColumn(
    index: Int,
    header: String,
    align: Align,
    minWidth: Int,
    maxWidth: Int,
) : TableColumn<String?>(
    index,
    header,
    align,
    minWidth,
    maxWidth,
) {

    override fun validateType(value: Any): Boolean {
        return value is String
    }

    override fun doGetRequestedWidth(value: String?): Int {
        return value?.length ?: 0
    }

    override fun doFormatValue(value: String?): String {
        return value ?: ""
    }

    override fun getValueFromRow(rowData: RowData): String? {
        return rowData[index] as String?
    }

    companion object


}

internal class NumberColumn(
    index: Int,
    header: String,
     align: Align,
     minWidth: Int,
     maxWidth: Int,
    private val precision: Int
) : TableColumn<Number?>(
    index,
    header,
    align,
    minWidth,
    maxWidth,
) {

    private val numberFormat: NumberFormat = DecimalFormat().apply {
        setMaximumFractionDigits(precision)
        isGroupingUsed = false
    }

    override fun validateType(value: Any): Boolean {
        return value is Number
    }

    override fun doGetRequestedWidth(value: Number?): Int {
        return if (value == null) 0 else (log10(value.toInt().toDouble()) + 1 + 1 + precision).toInt()
    }

    override fun doFormatValue(value: Number?): String {
        return numberFormat.format(value)
    }

    override fun getValueFromRow(rowData: RowData): Number? {
        return rowData[index] as Number?
    }

}

enum class Align { LEFT, CENTER_LEFT, CENTER_RIGHT, RIGHT }
