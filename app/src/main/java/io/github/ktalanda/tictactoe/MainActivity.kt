package io.github.ktalanda.tictactoe

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.playground

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reloadPlayground(listOf(
                "", "x", "",
                "o", "x", "",
                "", "o", "x"))
    }

    fun reloadPlayground(playgroundElements: List<String>) {
        playground.adapter = ArrayAdapter<String>(this, R.layout.playfield, playgroundElements)
    }
}
