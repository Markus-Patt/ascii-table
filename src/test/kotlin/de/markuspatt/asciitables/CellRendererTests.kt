package de.markuspatt.asciitables

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CellRendererTests : FunSpec({

    test("left aligned min max") {
        val cellRenderer = CellRenderer(
            minWidth = 5,
            maxWidth = 10,
            align = Align.LEFT
        )

        // @formatter:off
        cellRenderer.renderValue("")            shouldBe "     "
        cellRenderer.renderValue("1")           shouldBe "1    "
        cellRenderer.renderValue("12")          shouldBe "12   "
        cellRenderer.renderValue("123")         shouldBe "123  "
        cellRenderer.renderValue("1234")        shouldBe "1234 "
        cellRenderer.renderValue("12345")       shouldBe "12345"
        cellRenderer.renderValue("123456")      shouldBe "123456"
        cellRenderer.renderValue("1234567")     shouldBe "1234567"
        cellRenderer.renderValue("12345678")    shouldBe "12345678"
        cellRenderer.renderValue("123456789")   shouldBe "123456789"
        cellRenderer.renderValue("1234567890")  shouldBe "1234567890"
        cellRenderer.renderValue("12345678901") shouldBe "1234567890"
        // @formatter:on
    }

    test("right aligned min max") {
        val cellRenderer = CellRenderer(
            minWidth = 5,
            maxWidth = 10,
            align = Align.RIGHT
        )

        // @formatter:off
        cellRenderer.renderValue("")            shouldBe      "     "
        cellRenderer.renderValue("1")           shouldBe      "    1"
        cellRenderer.renderValue("12")          shouldBe      "   12"
        cellRenderer.renderValue("123")         shouldBe      "  123"
        cellRenderer.renderValue("1234")        shouldBe      " 1234"
        cellRenderer.renderValue("12345")       shouldBe      "12345"
        cellRenderer.renderValue("123456")      shouldBe     "123456"
        cellRenderer.renderValue("1234567")     shouldBe    "1234567"
        cellRenderer.renderValue("12345678")    shouldBe   "12345678"
        cellRenderer.renderValue("123456789")   shouldBe  "123456789"
        cellRenderer.renderValue("1234567890")  shouldBe "1234567890"
        cellRenderer.renderValue("12345678901") shouldBe "1234567890"
        // @formatter:on
    }

    test("center_left aligned min max") {
        val cellRenderer = CellRenderer(
            minWidth = 5,
            maxWidth = 10,
            align = Align.CENTER_LEFT
        )

        // @formatter:off
        cellRenderer.renderValue("")            shouldBe      "     "
        cellRenderer.renderValue("1")           shouldBe      "  1  "
        cellRenderer.renderValue("12")          shouldBe      " 12  "
        cellRenderer.renderValue("123")         shouldBe      " 123 "
        cellRenderer.renderValue("1234")        shouldBe      "1234 "
        cellRenderer.renderValue("12345")       shouldBe      "12345"
        cellRenderer.renderValue("123456")      shouldBe     "123456"
        cellRenderer.renderValue("1234567")     shouldBe    "1234567"
        cellRenderer.renderValue("12345678")    shouldBe   "12345678"
        cellRenderer.renderValue("123456789")   shouldBe  "123456789"
        cellRenderer.renderValue("1234567890")  shouldBe "1234567890"
        cellRenderer.renderValue("12345678901") shouldBe "1234567890"
        // @formatter:on
    }

    test("center_right aligned min max") {
        val cellRenderer = CellRenderer(
            minWidth = 5,
            maxWidth = 10,
            align = Align.CENTER_RIGHT
        )

        // @formatter:off
        cellRenderer.renderValue("")            shouldBe      "     "
        cellRenderer.renderValue("1")           shouldBe      "  1  "
        cellRenderer.renderValue("12")          shouldBe      "  12 "
        cellRenderer.renderValue("123")         shouldBe      " 123 "
        cellRenderer.renderValue("1234")        shouldBe      " 1234"
        cellRenderer.renderValue("12345")       shouldBe      "12345"
        cellRenderer.renderValue("123456")      shouldBe     "123456"
        cellRenderer.renderValue("1234567")     shouldBe    "1234567"
        cellRenderer.renderValue("12345678")    shouldBe   "12345678"
        cellRenderer.renderValue("123456789")   shouldBe  "123456789"
        cellRenderer.renderValue("1234567890")  shouldBe "1234567890"
        cellRenderer.renderValue("12345678901") shouldBe "1234567890"
        // @formatter:on
    }

})
