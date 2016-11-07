package io.github.delr3ves.dotto.core.model

sealed class MorseSymbol {
    abstract val representation: String

    object Dot: MorseSymbol() {
        override val representation = "."
    }
    object Dash: MorseSymbol() {
        override val representation = "-"
    }
    object LongSpace : MorseSymbol() {
        override val representation = "/"
    }
    object ShortSpace : MorseSymbol() {
        override val representation = " "
    }
}
