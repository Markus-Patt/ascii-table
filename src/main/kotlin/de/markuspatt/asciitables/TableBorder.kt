package de.markuspatt.asciitables


internal sealed class TableBorder(
    val vertical: Char?,
    val horizontal: Char?,
    val edge: Char?,
)

internal class TableBorderImpl(
    vertical: Char?,
    horizontal: Char?,
    edge: Char?,
) : TableBorder(
    vertical,
    horizontal,
    edge
)

internal object NoBorder : TableBorder(
    null,
    null,
    null,
)
