package io.github.ktalanda.tictactoe

object MainPresenter {
    var playgroundState: Array<Array<FieldType>>

    private val cleanPlayground = arrayOf(
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
            arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE))
    private lateinit var view: View

    init {
        playgroundState = cleanPlayground
    }

    fun bindView(view: View) {
        this.view = view
    }

    fun cleanPlayground() = reloadPlayground(cleanPlayground)

    fun reloadPlayground(playgroundState: Array<Array<FieldType>>) {
        if (playgroundState.size != 3) throw ArrayWrongSizeException()
        playgroundState.forEach { if (it.size != 3) throw ArrayWrongSizeException() }

        this.playgroundState = playgroundState
        view.reloadPlayground(playgroundState.flatMap { it.map { it.type } })
    }

    class ArrayWrongSizeException : Exception()

    interface View {
        fun reloadPlayground(playgroundViewElements: List<String>)
    }
}
