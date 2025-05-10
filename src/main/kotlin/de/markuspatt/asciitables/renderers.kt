package de.markuspatt.asciitables

import kotlin.math.max
import kotlin.math.min

internal class CellRenderer(
    val minWidth: Int,
    val maxWidth: Int,
    val align: Align,
) {

    fun render(value: String): String {
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
