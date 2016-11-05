package io.github.delr3ves.dotto

import android.app.Activity
import android.os.Bundle
import android.support.wearable.view.WatchViewStub
import android.widget.TextView

class MorseActivity : Activity() {

    private var mTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morse)
        val stub = findViewById(R.id.watch_view_stub) as WatchViewStub
        stub.setOnLayoutInflatedListener { stub -> mTextView = stub.findViewById(R.id.text) as TextView }
    }
}
