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

    fun getRequestedWidth(rowData: RowData): Int = doFormatValue(getValueFromRow(rowData)).length

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
    private val convertValue: (Any?) -> String? = { it?.toString() },
) : TableColumn<String?>(
    index,
    header,
    align,
    minWidth,
    maxWidth,
) {

    override fun validateType(value: Any): Boolean = convertValue(value) is String

    override fun doFormatValue(value: String?): String = value ?: ""

    override fun getValueFromRow(rowData: RowData): String? = convertValue(rowData[index])

}

internal class NumberColumn(
    index: Int,
    header: String,
    align: Align,
    minWidth: Int,
    maxWidth: Int,
    private val precision: Int,
    private val numberFormat: NumberFormat = defaultNumberFormat(precision),
) : TableColumn<Number?>(
    index,
    header,
    align,
    minWidth,
    maxWidth,
) {

    override fun validateType(value: Any): Boolean = value is Number

    override fun doFormatValue(value: Number?): String = numberFormat.format(value ?: 0)

    override fun getValueFromRow(rowData: RowData): Number? = rowData[index] as Number?

    companion object {
        internal fun defaultNumberFormat(precision: Int): DecimalFormat = DecimalFormat().apply {
            setMaximumFractionDigits(precision)
            isGroupingUsed = false
        }

    }

}


enum class Align { LEFT, CENTER_LEFT, CENTER_RIGHT, RIGHT }
