package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
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

        test("add with wrong column count throws") {
            val table = asciiTable {
                stringColumn("dummy 1")
            }

            assertThrows<IllegalArgumentException> {
                table.add("", "")
            }.shouldHaveMessage("wrong number of arguments: expected 1, got 2")
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
                
    
            """.trimIndent()
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
                      1
    
            """.trimIndent()
        }

        test("print one percent column with one value") {
            val asciiTable = asciiTable {
                percentColumn("dummy 1")
            }

            asciiTable.add(
                listOf(
                    0.54
                )
            )

            asciiTable.printToString() shouldBe """
                dummy 1
                   54 %
    
            """.trimIndent()
        }

        test("print two progressBar columns with two value") {
            val asciiTable = asciiTable {
                progressBarColumn("dummy 1")
                progressBarColumn("dummy 2") {
                    width = 20
                    barChar = 'x'
                }
            }

            asciiTable.add(
                0, 0.1
            )
            asciiTable.add(
                0.5, 1.0
            )

            asciiTable.printToString() shouldBe """
                dummy 1    dummy 2
                           xx
                =====      xxxxxxxxxxxxxxxxxxxx
    
            """.trimIndent()
        }

        test("print progressBar column with emptyChar") {
            val asciiTable = asciiTable {
                progressBarColumn("dummy 1") {
                    width = 10
                    barChar = 'x'
                    emptyChar = '-'
                }
            }

            asciiTable.add(0)
            asciiTable.add(0.5)
            asciiTable.add(1.0)

            asciiTable.printToString() shouldBe """
                dummy 1
                ----------
                xxxxx-----
                xxxxxxxxxx
    
            """.trimIndent()
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

        test("print two string columns with multiple values") {
            val asciiTable = asciiTable {
                stringColumn("dummy 1")
                stringColumn("dummy 2")
            }

            asciiTable.add("value 1.1", "value 2.1")
            asciiTable.add("value 1.2", "value 2.2")
            asciiTable.add("value 1.3", "value 2.3")

            asciiTable.printToString() shouldBe """
                dummy 1   dummy 2
                value 1.1 value 2.1
                value 1.2 value 2.2
                value 1.3 value 2.3
    
            """.trimIndent()

        }


    }

    context("alignments") {

        test("string center_right right") {
            val asciiTable = asciiTable {
                stringColumn("column 1") {
                    align = Align.CENTER_RIGHT
                }
                stringColumn("column 2") {
                    align = Align.RIGHT
                }
            }

            asciiTable.add("a", "b")
            asciiTable.add("aa", "bb")
            asciiTable.add("aaa", "bbb")
            asciiTable.add("aa a", "b bb")

            asciiTable.printToString() shouldBe """
                column 1 column 2
                ....a...........b
                ...aa..........bb
                ...aaa........bbb
                ..aa.a.......b.bb
    
            """.trimIndent().replace('.', ' ')

        }

        test("string center_left right") {
            val asciiTable = asciiTable {
                stringColumn("column 1") {
                    align = Align.CENTER_LEFT
                }
                stringColumn("column 2") {
                    align = Align.RIGHT
                }
            }

            asciiTable.add("a", "b")
            asciiTable.add("aa", "bb")
            asciiTable.add("aaa", "bbb")
            asciiTable.add("aa a", "b bb")

            asciiTable.printToString() shouldBe """
                column 1 column 2
                ...a............b
                ...aa..........bb
                ..aaa.........bbb
                ..aa.a.......b.bb
    
            """.trimIndent().replace('.', ' ')

        }

    }

})
