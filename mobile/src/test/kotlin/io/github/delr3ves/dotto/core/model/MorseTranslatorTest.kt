package io.github.delr3ves.dotto.core.model

import io.github.delr3ves.Dot.MorseTranslator
import io.github.delr3ves.dotto.core.model.MorseSymbol.*
import io.kotlintest.specs.FlatSpec

class MorseTranslatorTest : FlatSpec() {
    val translator = MorseTranslator()

    //To Morse
    init {
        "Simple character" should "be translated to morse sequence of codes" {
            translator.toMorse("a") shouldBe listOf(Dot, Dash)
        }

        "Upper case character" should "be translate as it was a lower case one" {
            translator.toMorse("Z") shouldBe listOf(Dash, Dash, Dot, Dot)
        }

        "Character with accents" should "be translated to morse without accent" {
            translator.toMorse("Ü") shouldBe listOf(Dot, Dot, Dash)
        }

        "Consonant with accents" should "be translated to morse without accent" {
            translator.toMorse("ñ") shouldBe listOf(Dash, Dot)
        }

        "Numbers" should "be translated" {
            translator.toMorse("0") shouldBe listOf(Dash, Dash, Dash, Dash, Dash)
        }

        "Sequence of characters" should "should add short space between charactes" {
            translator.toMorse("hi there") shouldBe listOf(
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot, Dot,
                    ShortSpace,
                    LongSpace,
                    ShortSpace,
                    Dash,
                    ShortSpace,
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot,
                    ShortSpace,
                    Dot, Dash, Dot,
                    ShortSpace,
                    Dot)
        }

        "Character not found" should "return empty list" {
            translator.toMorse("∫") shouldBe emptyList<MorseSymbol>()
        }
    }

    //From Morse
    init {
        "Simple sequence" should "be translated to proper character" {
            translator.fromMorse(listOf(Dot, Dash)) shouldBe "a"
        }

        "Long sequence" should "be translated to a few characters" {
            translator.fromMorse(listOf(
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot, Dot,
                    ShortSpace,
                    LongSpace,
                    ShortSpace,
                    Dash,
                    ShortSpace,
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot,
                    ShortSpace,
                    Dot, Dash, Dot,
                    ShortSpace,
                    Dot)
            ) shouldBe "hi there"
        }

        "Unknown sequence" should "be translated to empty string" {
            translator.fromMorse(listOf(
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot, Dot,
                    ShortSpace,
                    Dash, Dot, Dot, Dot, Dot, LongSpace,
                    ShortSpace,
                    LongSpace,
                    ShortSpace,
                    Dot, Dot, Dot, Dot,
                    ShortSpace,
                    Dot,
                    ShortSpace,
                    Dot, Dash, Dot,
                    ShortSpace,
                    Dot)
            ) shouldBe "hi here"
        }
    }

    //Round trip
    init {
        "Simple sentence" should "be the same after the round trip" {
            val sampleText = "this is a round trip"
            translator.fromMorse(translator.toMorse(sampleText)) shouldBe sampleText
        }
    }
}