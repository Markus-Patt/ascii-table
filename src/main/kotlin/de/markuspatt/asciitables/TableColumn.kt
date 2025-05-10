package de.markuspatt.asciitables

import java.text.DecimalFormat
import java.text.NumberFormat


internal abstract class TableColumn<T>(
    val index: Int,
    val header: String,
    val align: Align,
    val minWidth: Int,
    val maxWidth: Int,
) {

    fun validateValue(value: Any?): Boolean = (value == null) || validateType(value)

    abstract fun validateType(value: Any): Boolean

    fun getRequestedWidth(rowData: RowData): Int = doGetRequestedWidth(getValueFromRow(rowData))

    abstract fun doGetRequestedWidth(value: T): Int

    abstract fun doFormatValue(value: T): String

    abstract fun getValueFromRow(rowData: RowData): T

    fun formatValue(rowData: RowData): String = getValueFromRow(rowData)?.let { doFormatValue(it) } ?: ""

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

    override fun validateType(value: Any): Boolean = value is String

    override fun doGetRequestedWidth(value: String?): Int = value?.length ?: 0

    override fun doFormatValue(value: String?): String = value ?: ""

    override fun getValueFromRow(rowData: RowData): String? = rowData[index] as String?

}

internal class NumberColumn(
    index: Int,
    header: String,
    align: Align,
    minWidth: Int,
    maxWidth: Int,
    private val precision: Int,
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

    override fun validateType(value: Any): Boolean = value is Number

    override fun doGetRequestedWidth(value: Number?): Int = doFormatValue(value).length

    override fun doFormatValue(value: Number?): String = numberFormat.format(value ?: 0)

    override fun getValueFromRow(rowData: RowData): Number? = rowData[index] as Number?

}

enum class Align { LEFT, CENTER_LEFT, CENTER_RIGHT, RIGHT }
