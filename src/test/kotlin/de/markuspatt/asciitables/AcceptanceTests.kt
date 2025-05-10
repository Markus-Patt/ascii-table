package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AcceptanceTests : FunSpec({

    beforeTest {
    }

    test("print multiple column configurations without values") {
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
                align = Align.CENTER_RIGHT
                maxWidth = 10
            }
            longColumn("dummy 3") {
                align = Align.RIGHT
            }
            doubleColumn("dummy 3") {
                align = Align.RIGHT

                precision = 1
            }
        }

        asciiTable.printToString() shouldBe """
            dummy 1 dummy 2 dummy 3 dummy 3 dummy 3

        """.trimIndent()

    }


    test("print multiple column configurations with values") {
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
            }
            doubleColumn("dummy 3") {
                align = Align.RIGHT

                precision = 1
            }
        }

        asciiTable.add(
            "asd", "abc", "xyz", 1234567890, 1234567890.12345
        )
        asciiTable.add(
            null, null, null, null, null,
        )

        asciiTable.printToString() shouldBe """
            dummy 1 dummy 2 dummy 3    dummy 3      dummy 3
            asd       abc       xyz 1234567890 1234567890,1


        """.trimIndent()

    }

    context("borders") {
        test("no border") {
            val asciiTable = asciiTable {
                withoutBorder()

                stringColumn("dummy 1")
                longColumn("dummy 2") {
                    align = Align.RIGHT
                }
                doubleColumn("dummy 3") {
                    align = Align.RIGHT

                    precision = 1
                }
            }

            asciiTable.add(
                "asd", 1234567890, 1234567890.12345
            )

            asciiTable.printToString() shouldBe """
                dummy 1    dummy 2      dummy 3
                asd     1234567890 1234567890,1
    
            """.trimIndent()

        }

        test("default border") {

            val asciiTable = asciiTable {
                withBorder()

                stringColumn("dummy 1")
                longColumn("dummy 2") {
                    align = Align.RIGHT
                }
                doubleColumn("dummy 3") {
                    align = Align.RIGHT

                    precision = 1
                }
            }

            asciiTable.add(
                "asd", 1234567890, 1234567890.12345
            )

            asciiTable.printToString() shouldBe """
                dummy 1    dummy 2      dummy 3
                asd     1234567890 1234567890,1
    
            """.trimIndent()

        }
        test("custom border 1") {
            val asciiTable = asciiTable {
                withBorder {

                }

                stringColumn("dummy 1")
                longColumn("dummy 2") {
                    align = Align.RIGHT
                }
                doubleColumn("dummy 3") {
                    align = Align.RIGHT

                    precision = 1
                }
            }

            asciiTable.add(
                "asd", 1234567890, 1234567890.12345
            )

            asciiTable.printToString() shouldBe """
                dummy 1    dummy 2      dummy 3
                asd     1234567890 1234567890,1
    
            """.trimIndent()

        }
    }


})
