package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AcceptanceTests : FunSpec({

    beforeTest {
    }

    xtest("print one string column without values") {
        val asciiTable = asciiTable {
            stringColumn("dummy 1") {
                align = ColumnBuilder.Align.LEFT
            }
            stringColumn("dummy 2") {
                align = ColumnBuilder.Align.CENTER
                minWidth = 5
                maxWidth = 10
            }
            stringColumn("dummy 3") {
                align = ColumnBuilder.Align.RIGHT
                maxWidth = 10
            }
            longColumn("dummy 3") {
                align = ColumnBuilder.Align.RIGHT
                maxWidth = 10
            }
            doubleColumn("dummy 3", 2) {
                align = ColumnBuilder.Align.RIGHT
                maxWidth = 10

                precision = 1
            }
        }

        asciiTable.printToString() shouldBe """
            dummy 1

        """.trimIndent()

    }


})
