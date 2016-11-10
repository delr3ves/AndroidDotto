package io.github.delr3ves.dotto.flash

import android.os.AsyncTask
import io.github.delr3ves.dotto.core.model.IndicatorStatus.Off
import io.github.delr3ves.dotto.core.model.IndicatorStatus.On
import io.github.delr3ves.dotto.core.model.MorseSymbol

class MorseFlashTranslator(flashManager: CameraManager) : AsyncTask<List<MorseSymbol>, Void, Boolean>() {

    val cameraManager = flashManager

    override fun doInBackground(vararg morse: List<MorseSymbol>): Boolean {
        morse.forEach { it.forEach { lightSymbol(it) } }
        return true
    }

    private fun lightSymbol(symbol: MorseSymbol) {
        val baseDuration = 100L
        val flashAction = when (symbol) {
            is MorseSymbol.Dot -> Pair(1L, On)
            is MorseSymbol.Dash -> Pair(3L, On)
            is MorseSymbol.ShortSpace -> Pair(2L, Off)
            is MorseSymbol.LongSpace -> Pair(3L, Off)
        }

        cameraManager.getFlashManager()?.setStatus(flashAction.second)
        Thread.sleep(baseDuration * flashAction.first)
        cameraManager.getFlashManager()?.toggle()
    }

}



