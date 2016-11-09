package io.github.delr3ves.dotto

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import io.github.delr3ves.Dot.MorseTranslator
import io.github.delr3ves.dotto.core.model.MorseSymbol
import io.github.delr3ves.dotto.flash.CameraManager
import io.github.delr3ves.dotto.flash.exceptions.FlashNotAvailableException
import javax.inject.Inject

class MorseGeneratorActivity : AppCompatActivity() {
    @Inject
    lateinit var cameraManager: CameraManager

    private val translator = MorseTranslator()
    lateinit var unbinder: Unbinder

    @BindView(R.id.morse_input)
    lateinit var input: EditText

    @BindView(R.id.morse_translation)
    lateinit var translation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morse_generator)
        ButterKnife.setDebug(true)
        unbinder = ButterKnife.bind(this)

        (application as (DottoApplication)).component.inject(this)

        try {
            cameraManager.initializeFlashIfNeeded()
        } catch (e: FlashNotAvailableException) {
            Toast.makeText(this, R.string.error_flash_not_available, Toast.LENGTH_LONG)
        }
        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(onMorseGenerate())

    }

    private fun onMorseGenerate(): (View) -> Unit {
        return { view ->
            translation.setText("")
            val morse = translator.toMorse(input.text.toString())
            morse.forEach {
                lightSymbol(it)
                appendSymbol(it)
            }
        }
    }

    private fun appendSymbol(symbol: MorseSymbol) {
        val newText = translation.text.toString() + symbol.representation
        translation.setText(newText)
    }

    private fun lightSymbol(symbol: MorseSymbol): Boolean {
        val baseDuration = 100L
        val relativeDuration = when (symbol) {
            is MorseSymbol.Dot -> 1L
            is MorseSymbol.Dash -> 3L
            is MorseSymbol.ShortSpace -> 2L
            is MorseSymbol.LongSpace -> 3L
        }
        cameraManager.getFlashManager()?.turnOn()
        return Handler().postDelayed({
            cameraManager.getFlashManager()?.turnOff()
        }, baseDuration * relativeDuration)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraManager.closeCamera()
        unbinder.unbind()
    }

}
