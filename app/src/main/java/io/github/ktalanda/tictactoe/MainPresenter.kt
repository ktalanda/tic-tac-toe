package io.github.ktalanda.tictactoe

object MainPresenter {
    var gameBoardState: Array<Array<FieldType>>
    var currentPlayerState: FieldType

    val cleanGameBoard = arrayOf(
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE))
    private lateinit var view: View

    init {
        gameBoardState = cleanGameBoard
        currentPlayerState = FieldType.X
    }

    fun bindView(view: View) {
        this.view = view
    }

    fun reloadGameBoard(playgroundState: Array<Array<FieldType>>) {
        if (playgroundState.size != 3) throw ArrayWrongSizeException()
        playgroundState.forEach { if (it.size != 3) throw ArrayWrongSizeException() }

        this.gameBoardState = playgroundState
        view.reloadGameBoard(playgroundState.flatMap { it.map { it.type } })
    }

    fun clickField(position: Int) {
        val clickedColumn = position / 3
        val clickedRow = position % 3

        if (gameBoardState[clickedColumn][clickedRow] != FieldType.NONE) return

        gameBoardState[clickedColumn][clickedRow] = currentPlayerState
        reloadGameBoard(gameBoardState)
        currentPlayerState = nextPlayer(currentPlayerState)
    }

    fun nextPlayer(player: FieldType): FieldType =
            when (player) {
                FieldType.X -> FieldType.O
                else -> FieldType.X
            }

    class ArrayWrongSizeException : Exception()

    fun checkWinner(gameBoardState: Array<Array<FieldType>>): FieldType {
        if (isWinner(FieldType.X, gameBoardState)) return FieldType.X
        if (isWinner(FieldType.O, gameBoardState)) return FieldType.O

        return FieldType.NONE
    }

    private fun isWinner(type: FieldType, gameBoardState: Array<Array<FieldType>>): Boolean =
            isWinnerInRow(type, gameBoardState)
                    || isWinnerInColumn(type, gameBoardState)
                    || isWinnerInDiagonal(type, gameBoardState)

    private fun isWinnerInRow(type: FieldType, gameBoardState: Array<Array<FieldType>>): Boolean =
            gameBoardState.any { it.filter { it == type }.size == 3 }

    private fun isWinnerInColumn(type: FieldType, gameBoardState: Array<Array<FieldType>>): Boolean =
            (0 until gameBoardState.size).any {
                gameBoardState[0][it] == gameBoardState[1][it]
                        && gameBoardState[1][it] == gameBoardState[2][it]
                        && gameBoardState[0][it] == type
            }

    private fun isWinnerInDiagonal(type: FieldType, gameBoardState: Array<Array<FieldType>>): Boolean =
            (gameBoardState[0][0] == gameBoardState[1][1] && gameBoardState[1][1] == gameBoardState[2][2]
                    || gameBoardState[0][2] == gameBoardState[1][1] && gameBoardState[1][1] == gameBoardState[2][0])
                    && gameBoardState[1][1] == type

    fun isDraw(gameBoardState: Array<Array<FieldType>>): Boolean =
            !gameBoardState.any { it.any { it == FieldType.NONE } } && checkWinner(gameBoardState) == FieldType.NONE

    interface View {
        fun reloadGameBoard(playgroundViewElements: List<String>)
    }
}
