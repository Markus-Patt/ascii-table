package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class AsciiTableTest : FunSpec({

    beforeTest {
    }

    context("common") {
        test("building without colum throws") {
            assertThrows<IllegalStateException> {
                asciiTable {

                }
            }
        }

        test("add string value to long column throws") {
            val asciiTable = asciiTable {
                longColumn("dummy 1")
            }

            assertThrows<IllegalArgumentException> {
                asciiTable.add(
                    listOf(
                        "value 1"
                    )
                )
            }
        }

        test("add int value to long column does not throw") {
            val asciiTable = asciiTable {
                longColumn("dummy 1")
            }

            assertDoesNotThrow {
                asciiTable.add(
                    listOf(
                        1
                    )
                )
            }
        }
    }

    context("single column") {
        test("print one string column without values") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
            }

            asciiTable.printToString() shouldBe """
            dummy 1

        """.trimIndent()

        }

        test("print one string column with one value") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
            }

            asciiTable.add(
                listOf(
                    "value 1"
                )
            )

            asciiTable.printToString() shouldBe """
            dummy 1
            value 1

        """.trimIndent()

        }

        test("print one string column with null value") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
            }

            asciiTable.add(
                listOf(
                    null
                )
            )

            asciiTable.printToString() shouldBe """
            dummy 1
            ~~~~~~~

        """.trimIndent().replace('~', ' ')
        }

        test("print one long column with one value") {
            val asciiTable = asciiTable {
                longColumn("dummy 1")
            }

            asciiTable.add(
                listOf(
                    1L
                )
            )

            asciiTable.printToString() shouldBe """
            dummy 1
            1~~~~~~

        """.trimIndent().replace('~', ' ')
        }

    }

    context("multiple columns") {
        test("print two string columns without values") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
                stringColumn("dummy 2")
            }

            asciiTable.printToString() shouldBe """
            dummy 1 dummy 2

        """.trimIndent()

        }

        test("print two string columns with one value") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
                stringColumn("dummy 2")
            }

            asciiTable.add(
                "value 1",
                "value 2",
            )

            asciiTable.printToString() shouldBe """
            dummy 1 dummy 2
            value 1 value 2

        """.trimIndent()

        }


    }

})
