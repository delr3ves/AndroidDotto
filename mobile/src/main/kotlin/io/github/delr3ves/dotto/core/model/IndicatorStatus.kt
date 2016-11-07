package io.github.delr3ves.dotto.core.model

sealed class IndicatorStatus {
    class On: IndicatorStatus()
    class Off: IndicatorStatus()
}