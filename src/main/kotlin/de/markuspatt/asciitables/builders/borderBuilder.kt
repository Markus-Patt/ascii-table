package de.markuspatt.asciitables.builders

import de.markuspatt.asciitables.TableBorder
import de.markuspatt.asciitables.TableBorderImpl

interface BorderBuilder {

    var vertical: Char?

    var horizontal: Char?

    var edge: Char?

}

internal class BorderBuilderImpl() : BorderBuilder {

    override var vertical: Char? = '|'
    override var horizontal: Char? = '-'
    override var edge: Char? = '+'

    fun build(): TableBorder {
        validate()

        return TableBorderImpl(vertical, horizontal, edge)
    }

    private fun validate() {
        check((edge != null) || (vertical == null) || (horizontal == null)) {
            "if edge is not set, vertical and horizontal cannot be set both"
        }
    }

}

