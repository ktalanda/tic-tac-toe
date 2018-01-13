package io.github.ktalanda.tictactoe

import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock private lateinit var view: MainPresenter.View
    private val presenter: MainPresenter = MainPresenter

    @Before
    fun setUp() {
        presenter.bindView(view)
    }

    @Test
    fun whenReloadPlayground_thenShouldReloadViewAndUpdateState() {
        presenter.reloadPlayground(
                arrayOf(
                        arrayOf(FieldType.X, FieldType.X, FieldType.O),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        )
        verify(view, times(1)).reloadPlayground(listOf("x", "x", "o", "o", "", "", "x", "", ""))
        Assert.assertTrue(presenter.playgroundState[0]
                .contentEquals(arrayOf(FieldType.X, FieldType.X, FieldType.O)))
        Assert.assertTrue(presenter.playgroundState[1]
                .contentEquals(arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.playgroundState[2]
                .contentEquals(arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)))

        presenter.reloadPlayground(
                arrayOf(
                        arrayOf(FieldType.O, FieldType.O, FieldType.O),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)
                )
        )
        verify(view, times(1)).reloadPlayground(listOf("o", "o", "o", "x", "", "", "o", "", ""))
        Assert.assertTrue(presenter.playgroundState[0]
                .contentEquals(arrayOf(FieldType.O, FieldType.O, FieldType.O)))
        Assert.assertTrue(presenter.playgroundState[1]
                .contentEquals(arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.playgroundState[2]
                .contentEquals(arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)))
    }

    @Test
    fun whenClean_thenReloadPlaygroundViewWithListWithEmptyElements() {
        presenter.cleanPlayground()

        verify(view, times(1)).reloadPlayground(listOf("", "", "", "", "", "", "", "", ""))
        Assert.assertTrue(presenter.playgroundState[0]
                .contentEquals(arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.playgroundState[1]
                .contentEquals(arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.playgroundState[2]
                .contentEquals(arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun givenArrayWithLessThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadPlayground(arrayOf())
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun given3ArraysWithLessThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadPlayground(arrayOf(arrayOf(), arrayOf(), arrayOf()))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun givenArrayWithMoreThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadPlayground(arrayOf(arrayOf(), arrayOf(), arrayOf(), arrayOf()))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun given3ArraysWithMoreThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadPlayground(
                arrayOf(
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE, FieldType.NONE)))
    }
}