package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AcceptanceTests : FunSpec({

    beforeTest {
    }

    test("print one string column without values") {
        val asciiTable = asciiTable {
            stringColumn("dummy 1") {
                align = Align.LEFT
            }
            stringColumn("dummy 2") {
                align = Align.CENTER_LEFT
                minWidth = 5
                maxWidth = 10
            }
            stringColumn("dummy 3") {
                align = Align.RIGHT
                maxWidth = 10
            }
            longColumn("dummy 3") {
                align = Align.RIGHT
                maxWidth = 10
            }
            doubleColumn("dummy 3", 2) {
                align = Align.RIGHT
                maxWidth = 10

                precision = 1
            }
        }

        asciiTable.printToString() shouldBe """
            dummy 1 dummy 2 dummy 3 dummy 3 dummy 3

        """.trimIndent()

    }


})
