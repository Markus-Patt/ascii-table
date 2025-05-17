package de.markuspatt.asciitables


internal sealed class TableBorder(
    val vertical: Char?,
    val horizontal: Char?,
    val edge: Char?,
    val innerBorder: Boolean,
)

internal class TableBorderImpl(
    vertical: Char?,
    horizontal: Char?,
    edge: Char?,
    innerBorder: Boolean,
) : TableBorder(
    vertical,
    horizontal,
    edge,
    innerBorder,
)

internal object NoBorder : TableBorder(
    null,
    null,
    null,
    false,
)
