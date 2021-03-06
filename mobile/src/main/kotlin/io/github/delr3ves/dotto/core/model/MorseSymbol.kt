package io.github.delr3ves.dotto.core.model

sealed class MorseSymbol {
    abstract val representation: String

    override fun toString(): String {
        return representation
    }

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
