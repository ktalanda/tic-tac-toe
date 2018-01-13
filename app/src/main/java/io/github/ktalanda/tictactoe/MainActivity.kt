package io.github.ktalanda.tictactoe

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.playground

class MainActivity(private val presenter: MainPresenter = MainPresenter)
    : Activity(), MainPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.bindView(this)

        presenter.reloadPlayground(presenter.playgroundState)
    }

    override fun reloadPlayground(playgroundViewElements: List<String>) {
        playground.adapter = ArrayAdapter<String>(this, R.layout.playfield, playgroundViewElements)
    }
}
