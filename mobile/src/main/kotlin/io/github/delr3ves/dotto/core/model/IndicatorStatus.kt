package io.github.delr3ves.dotto.core.model

sealed class IndicatorStatus {
    object On: IndicatorStatus()
    object Off: IndicatorStatus()
}