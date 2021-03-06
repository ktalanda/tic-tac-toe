package io.github.ktalanda.tictactoe

object MainPresenter {
    var gameBoardState: List<List<FieldType>>
    var currentPlayerState: FieldType

    val cleanGameBoard = listOf(
            listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE))
    private lateinit var view: View

    init {
        gameBoardState = cleanGameBoard
        currentPlayerState = FieldType.X
    }

    fun bindView(view: View) {
        this.view = view
    }

    fun reloadGameBoard(playgroundState: List<List<FieldType>>) {
        if (playgroundState.size != 3) throw GameBoardWrongSizeException()
        playgroundState.forEach { if (it.size != 3) throw GameBoardWrongSizeException() }

        this.gameBoardState = playgroundState
        view.reloadGameBoard(playgroundState.flatMap { it.map { it.type } })
    }

    fun clickField(position: Int) {
        val clickedColumn = position / 3
        val clickedRow = position % 3

        if (gameBoardState[clickedColumn][clickedRow] != FieldType.NONE) return

        gameBoardState = getUpdatedGameBoardState(clickedColumn, clickedRow, currentPlayerState, gameBoardState)

        reloadGameBoard(gameBoardState)
        currentPlayerState = nextPlayer(currentPlayerState)

        val winner = checkWinner(gameBoardState)
        if (winner != FieldType.NONE) {
            view.showWinner(winner.type)
            reloadGameBoard(cleanGameBoard)
        }
        if (isDraw(gameBoardState)) {
            view.showDraw()
            reloadGameBoard(cleanGameBoard)
        }
    }

    fun nextPlayer(player: FieldType): FieldType =
            when (player) {
                FieldType.X -> FieldType.O
                else -> FieldType.X
            }

    fun checkWinner(gameBoardState: List<List<FieldType>>): FieldType {
        if (isWinner(FieldType.X, gameBoardState)) return FieldType.X
        if (isWinner(FieldType.O, gameBoardState)) return FieldType.O

        return FieldType.NONE
    }

    private fun isWinner(type: FieldType, gameBoardState: List<List<FieldType>>): Boolean =
            isWinnerInRow(type, gameBoardState)
                    || isWinnerInColumn(type, gameBoardState)
                    || isWinnerInDiagonal(type, gameBoardState)

    private fun isWinnerInRow(type: FieldType, gameBoardState: List<List<FieldType>>): Boolean =
            gameBoardState.any { it.filter { it == type }.size == 3 }

    private fun isWinnerInColumn(type: FieldType, gameBoardState: List<List<FieldType>>): Boolean =
            (0 until gameBoardState.size).any {
                gameBoardState[0][it] == gameBoardState[1][it]
                        && gameBoardState[1][it] == gameBoardState[2][it]
                        && gameBoardState[0][it] == type
            }

    private fun isWinnerInDiagonal(type: FieldType, gameBoardState: List<List<FieldType>>): Boolean =
            (gameBoardState[0][0] == gameBoardState[1][1] && gameBoardState[1][1] == gameBoardState[2][2]
                    || gameBoardState[0][2] == gameBoardState[1][1] && gameBoardState[1][1] == gameBoardState[2][0])
                    && gameBoardState[1][1] == type

    fun isDraw(gameBoardState: List<List<FieldType>>): Boolean =
            !gameBoardState.any { it.any { it == FieldType.NONE } } && checkWinner(gameBoardState) == FieldType.NONE

    private fun getUpdatedGameBoardState(
            column: Int,
            row: Int,
            type: FieldType,
            inputGameBoardState: List<List<FieldType>>): List<List<FieldType>> {
        val gameBoardStateArray = inputGameBoardState.toTypedArray().map { it.toTypedArray() }
        gameBoardStateArray[column][row] = type
        return gameBoardStateArray.toList().map { it.toList() }
    }

    class GameBoardWrongSizeException : Exception()

    interface View {
        fun reloadGameBoard(playgroundViewElements: List<String>)
        fun showWinner(winner: String)
        fun showDraw()
    }
}
