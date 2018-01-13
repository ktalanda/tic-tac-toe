package io.github.ktalanda.tictactoe

object MainPresenter {
    var gameBoardState: Array<Array<FieldType>>

    private val cleanGameBoard = arrayOf(
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE))
    private lateinit var view: View

    init {
        gameBoardState = cleanGameBoard
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

    class ArrayWrongSizeException : Exception()

    interface View {
        fun reloadGameBoard(playgroundViewElements: List<String>)
    }
}
