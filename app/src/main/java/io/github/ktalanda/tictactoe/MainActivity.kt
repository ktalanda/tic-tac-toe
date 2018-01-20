package io.github.ktalanda.tictactoe

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.gameBoard

class MainActivity(private val presenter: MainPresenter = MainPresenter)
    : Activity(), MainPresenter.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.bindView(this)

        presenter.reloadGameBoard(presenter.gameBoardState)

        gameBoard.setOnItemClickListener { _, _, position, _ -> presenter.clickField(position) }
    }

    override fun reloadGameBoard(playgroundViewElements: List<String>) {
        gameBoard.adapter = ArrayAdapter<String>(this, R.layout.playfield, playgroundViewElements)
    }

    override fun showWinner(winner: String) {
        Toast.makeText(this, "Winner is " + winner, Toast.LENGTH_LONG).show()
    }

    override fun showDraw() {
        Toast.makeText(this, "Draw", Toast.LENGTH_LONG).show()
    }
}
