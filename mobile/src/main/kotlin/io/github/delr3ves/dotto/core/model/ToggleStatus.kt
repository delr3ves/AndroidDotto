package io.github.delr3ves.dotto.core.model

sealed class ToggleStatus {
    class On: ToggleStatus()
    class Off: ToggleStatus()
}