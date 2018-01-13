package io.github.ktalanda.tictactoe

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.gameBoard

class MainActivity(private val presenter: MainPresenter = MainPresenter)
    : Activity(), MainPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.bindView(this)

        presenter.reloadGameBoard(presenter.gameBoardState)
    }

    override fun reloadGameBoard(playgroundViewElements: List<String>) {
        gameBoard.adapter = ArrayAdapter<String>(this, R.layout.playfield, playgroundViewElements)
    }
}
