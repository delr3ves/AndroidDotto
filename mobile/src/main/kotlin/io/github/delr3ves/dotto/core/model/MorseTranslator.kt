package io.github.delr3ves.Dot

import io.github.delr3ves.dotto.core.model.MorseSymbol
import io.github.delr3ves.dotto.core.model.MorseSymbol.*
import io.github.delr3ves.dotto.core.extensions.CharExtensions.normalize

class MorseTranslator {

    private val charToMorseSymbol = mapOf(
            'a' to listOf(Dot, Dash),
            'b' to listOf(Dash, Dot, Dot, Dot),
            'c' to listOf(Dash, Dot, Dash, Dot),
            'd' to listOf(Dash, Dot, Dot),
            'e' to listOf(Dot),
            'f' to listOf(Dot, Dot, Dash, Dot),
            'g' to listOf(Dash, Dash, Dot),
            'h' to listOf(Dot, Dot, Dot, Dot),
            'i' to listOf(Dot, Dot),
            'j' to listOf(Dot, Dash, Dash, Dash),
            'k' to listOf(Dash, Dot, Dash),
            'l' to listOf(Dot, Dash, Dot, Dot),
            'm' to listOf(Dash, Dash),
            'n' to listOf(Dash, Dot),
            'o' to listOf(Dash, Dash, Dash),
            'p' to listOf(Dot, Dash, Dash, Dot),
            'q' to listOf(Dash, Dash, Dot, Dash),
            'r' to listOf(Dot, Dash, Dot),
            's' to listOf(Dot, Dot, Dot),
            't' to listOf(Dash),
            'u' to listOf(Dot, Dot, Dash),
            'v' to listOf(Dot, Dot, Dot, Dash),
            'w' to listOf(Dot, Dash, Dash),
            'x' to listOf(Dash, Dot, Dot, Dash),
            'y' to listOf(Dash, Dot, Dash, Dash),
            'z' to listOf(Dash, Dash, Dot, Dot),
            ' ' to listOf(LongSpace),
            '0' to listOf(Dash, Dash, Dash, Dash, Dash),
            '1' to listOf(Dot, Dash, Dash, Dash, Dash),
            '2' to listOf(Dot, Dot, Dash, Dash, Dash),
            '3' to listOf(Dot, Dot, Dot, Dash, Dash),
            '4' to listOf(Dot, Dot, Dot, Dot, Dash),
            '5' to listOf(Dot, Dot, Dot, Dot, Dot),
            '6' to listOf(Dash, Dot, Dot, Dot, Dot),
            '7' to listOf(Dash, Dash, Dot, Dot, Dot),
            '8' to listOf(Dash, Dash, Dash, Dot, Dot),
            '9' to listOf(Dash, Dash, Dash, Dash, Dot))

    fun toMorse(text: String): List<MorseSymbol> {
        val morse = text.trim().flatMap { letter ->
            val morseSequence = charToMorseSymbol.get(letter.normalize())
            (morseSequence.orEmpty() + ShortSpace).asIterable()
        }
        return morse.orEmpty().dropLastWhile { it.equals(ShortSpace) }
    }

    fun fromMorse(morse: List<MorseSymbol>): String {
        return fromMorseChunks(splitInMorseCharacters(morse))
    }

    private fun fromMorseChunks(morse: List<List<MorseSymbol>>): String {
        return morse.map { symbol ->
            morseSymbolToChar.getOrElse(symbol) {""}
        }.joinToString("")
    }

    private val morseSymbolToChar: Map<List<MorseSymbol>, Char> =
            charToMorseSymbol.map { entry ->
                Pair(entry.value, entry.key)
            }.toMap()

    private fun splitInMorseCharacters(morse: List<MorseSymbol>): List<List<MorseSymbol>>{
        val chunks = mutableListOf<List<MorseSymbol>>()
        var currentChunk = mutableListOf<MorseSymbol>()
        morse.forEach { item ->
            when (item) {
                is ShortSpace -> {
                    chunks.add(currentChunk)
                    currentChunk = mutableListOf()
                }
                else -> currentChunk.add(item)
            }
        }
        if (currentChunk.isNotEmpty()) {
            chunks.add(currentChunk)
        }
        return chunks
    }

}