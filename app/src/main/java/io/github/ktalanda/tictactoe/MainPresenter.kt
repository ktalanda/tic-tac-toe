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
        currentPlayerState = FieldType.NONE
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

    interface View {
        fun reloadGameBoard(playgroundViewElements: List<String>)
    }
}
