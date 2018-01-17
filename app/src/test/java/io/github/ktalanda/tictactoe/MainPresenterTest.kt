package io.github.ktalanda.tictactoe

import com.nhaarman.mockito_kotlin.any
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

    @Mock private lateinit var viewMock: MainPresenter.View
    private val presenter: MainPresenter = MainPresenter

    @Before
    fun setUp() {
        presenter.bindView(viewMock)
        presenter.gameBoardState = arrayOf(
                arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE))
    }

    @Test
    fun whenReloadPlayground_thenShouldReloadViewAndUpdateState() {
        presenter.reloadGameBoard(
                arrayOf(
                        arrayOf(FieldType.X, FieldType.X, FieldType.O),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        )
        verify(viewMock, times(1)).reloadGameBoard(listOf("x", "x", "o", "o", "", "", "x", "", ""))
        Assert.assertTrue(presenter.gameBoardState[0]
                .contentEquals(arrayOf(FieldType.X, FieldType.X, FieldType.O)))
        Assert.assertTrue(presenter.gameBoardState[1]
                .contentEquals(arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.gameBoardState[2]
                .contentEquals(arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)))

        presenter.reloadGameBoard(
                arrayOf(
                        arrayOf(FieldType.O, FieldType.O, FieldType.O),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)
                )
        )
        verify(viewMock, times(1)).reloadGameBoard(listOf("o", "o", "o", "x", "", "", "o", "", ""))
        Assert.assertTrue(presenter.gameBoardState[0]
                .contentEquals(arrayOf(FieldType.O, FieldType.O, FieldType.O)))
        Assert.assertTrue(presenter.gameBoardState[1]
                .contentEquals(arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.gameBoardState[2]
                .contentEquals(arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun givenArrayWithLessThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(arrayOf())
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun given3ArraysWithLessThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(arrayOf(arrayOf(), arrayOf(), arrayOf()))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun givenArrayWithMoreThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(arrayOf(arrayOf(), arrayOf(), arrayOf(), arrayOf()))
    }

    @Test(expected = MainPresenter.ArrayWrongSizeException::class)
    fun given3ArraysWithMoreThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(
                arrayOf(
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE, FieldType.NONE)))
    }

    @Test
    fun givenNone_whenNextPlayer_thenReturnX() {
        Assert.assertEquals("X should be next after NONE", FieldType.X, presenter.nextPlayer(FieldType.NONE))
        Assert.assertEquals("O should be next after X", FieldType.O, presenter.nextPlayer(FieldType.X))
        Assert.assertEquals("X should be next after O", FieldType.X, presenter.nextPlayer(FieldType.O))
    }

    @Test
    fun givenValidPosition_whenClickField_thenUpdateGameBoardState() {
        presenter.gameBoardState = presenter.cleanGameBoard.clone()
        presenter.currentPlayerState = FieldType.X
        presenter.clickField(1)
        presenter.clickField(3)
        Assert.assertTrue(presenter.gameBoardState[0]
                .contentEquals(arrayOf(FieldType.NONE, FieldType.X, FieldType.NONE)))
        Assert.assertTrue(presenter.gameBoardState[1]
                .contentEquals(arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE)))
        Assert.assertTrue(presenter.gameBoardState[2]
                .contentEquals(arrayOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)))
    }

    @Test(expected = ArrayIndexOutOfBoundsException::class)
    fun givenToBigInvalidPosition_whenClickField_thenThrowsException() {
        presenter.clickField(9)
    }

    @Test
    fun whenClickField_thenReloadsGameboardOnView() {
        presenter.clickField(1)
        verify(viewMock, times(1)).reloadGameBoard(any())
    }

    @Test
    fun givenTakenField_whenClickField_thenDoesnotOverride() {
        presenter.currentPlayerState = FieldType.X
        presenter.gameBoardState =
                arrayOf(
                        arrayOf(FieldType.X, FieldType.O, FieldType.O),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        presenter.clickField(1)
        Assert.assertEquals(FieldType.O, presenter.gameBoardState[0][1])
    }

    @Test
    fun givenTakenField_whenClickField_thenDoesnotChangePlayer() {
        presenter.currentPlayerState = FieldType.X
        presenter.gameBoardState =
                arrayOf(
                        arrayOf(FieldType.X, FieldType.O, FieldType.O),
                        arrayOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        arrayOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        presenter.clickField(1)
        Assert.assertEquals(FieldType.X, presenter.currentPlayerState)
    }
}
