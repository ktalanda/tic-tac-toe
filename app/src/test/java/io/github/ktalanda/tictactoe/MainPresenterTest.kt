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
        presenter.gameBoardState = presenter.cleanGameBoard
    }

    @Test
    fun whenReloadGameBoard_thenShouldReloadViewAndUpdateState() {
        val gameBoardStateFirstFake = listOf(
                listOf(FieldType.X, FieldType.X, FieldType.O),
                listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
        )
        presenter.reloadGameBoard(gameBoardStateFirstFake)

        verify(viewMock, times(1)).reloadGameBoard(listOf("x", "x", "o", "o", "", "", "x", "", ""))
        Assert.assertEquals(gameBoardStateFirstFake, presenter.gameBoardState)

        val gameBoardStateSecondFake = listOf(
                listOf(FieldType.O, FieldType.O, FieldType.O),
                listOf(FieldType.X, FieldType.NONE, FieldType.NONE),
                listOf(FieldType.O, FieldType.NONE, FieldType.NONE)
        )
        presenter.reloadGameBoard(gameBoardStateSecondFake)

        verify(viewMock, times(1)).reloadGameBoard(listOf("o", "o", "o", "x", "", "", "o", "", ""))
        Assert.assertEquals(gameBoardStateSecondFake, presenter.gameBoardState)
    }

    @Test(expected = MainPresenter.GameBoardWrongSizeException::class)
    fun givenArrayWithLessThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(listOf())
    }

    @Test(expected = MainPresenter.GameBoardWrongSizeException::class)
    fun given3ArraysWithLessThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(listOf(listOf(), listOf(), listOf()))
    }

    @Test(expected = MainPresenter.GameBoardWrongSizeException::class)
    fun givenArrayWithMoreThan3Elements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(listOf(listOf(), listOf(), listOf(), listOf()))
    }

    @Test(expected = MainPresenter.GameBoardWrongSizeException::class)
    fun given3ArraysWithMoreThan3Subelements_whenReloadPlayground_thenThrowException() {
        presenter.reloadGameBoard(
                listOf(
                        listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE, FieldType.NONE)))
    }

    @Test
    fun givenNone_whenNextPlayer_thenReturnX() {
        Assert.assertEquals("X should be next after NONE", FieldType.X, presenter.nextPlayer(FieldType.NONE))
        Assert.assertEquals("O should be next after X", FieldType.O, presenter.nextPlayer(FieldType.X))
        Assert.assertEquals("X should be next after O", FieldType.X, presenter.nextPlayer(FieldType.O))
    }

    @Test
    fun givenValidPosition_whenClickField_thenUpdateGameBoardState() {
        presenter.currentPlayerState = FieldType.X
        presenter.clickField(1)
        presenter.clickField(3)

        val expected = listOf(
                listOf(FieldType.NONE, FieldType.X, FieldType.NONE),
                listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)
        )

        Assert.assertEquals(expected, presenter.gameBoardState)
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
                listOf(
                        listOf(FieldType.X, FieldType.O, FieldType.O),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        presenter.clickField(1)
        Assert.assertEquals(FieldType.O, presenter.gameBoardState[0][1])
    }

    @Test
    fun givenTakenField_whenClickField_thenDoesnotChangePlayer() {
        presenter.currentPlayerState = FieldType.X
        presenter.gameBoardState =
                listOf(
                        listOf(FieldType.X, FieldType.O, FieldType.O),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        presenter.clickField(1)
        Assert.assertEquals(FieldType.X, presenter.currentPlayerState)
    }

    @Test
    fun givenThreeSameInRow_whenGetWinner_thenReturnsWinner() {
        val gameBoardStateXWinFake =
                listOf(
                        listOf(FieldType.X, FieldType.X, FieldType.X),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )

        Assert.assertEquals("X should win if it occupies all 3 fields in first row.",
                FieldType.X, presenter.checkWinner(gameBoardStateXWinFake))

        val gameBoardStateOWinFake =
                listOf(
                        listOf(FieldType.NONE, FieldType.X, FieldType.X),
                        listOf(FieldType.O, FieldType.O, FieldType.O),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )

        Assert.assertEquals("O should win if it occupies all 3 fields in second row.",
                FieldType.O, presenter.checkWinner(gameBoardStateOWinFake))
    }

    @Test
    fun givenThreeSameInColumn_whenGetWinner_thenReturnsWinner() {
        val gameBoardStateXWinFake =
                listOf(
                        listOf(FieldType.X, FieldType.O, FieldType.X),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )

        Assert.assertEquals("X should win if it occupies all 3 fields in first column.",
                FieldType.X, presenter.checkWinner(gameBoardStateXWinFake))

        val gameBoardStateOWinFake =
                listOf(
                        listOf(FieldType.NONE, FieldType.O, FieldType.X),
                        listOf(FieldType.O, FieldType.O, FieldType.X),
                        listOf(FieldType.X, FieldType.O, FieldType.NONE)
                )

        Assert.assertEquals("O should win if it occupies all 3 fields in second column.",
                FieldType.O, presenter.checkWinner(gameBoardStateOWinFake))
    }

    @Test
    fun givenThreeSameInDiagonal_whenGetWinner_thenReturnsWinner() {
        val gameBoardStateXWinFake =
                listOf(
                        listOf(FieldType.X, FieldType.O, FieldType.X),
                        listOf(FieldType.X, FieldType.X, FieldType.NONE),
                        listOf(FieldType.O, FieldType.NONE, FieldType.X)
                )

        Assert.assertEquals("X should win if it occupies all 3 fields in diagonal.",
                FieldType.X, presenter.checkWinner(gameBoardStateXWinFake))

        val gameBoardStateOWinFake =
                listOf(
                        listOf(FieldType.NONE, FieldType.O, FieldType.O),
                        listOf(FieldType.O, FieldType.O, FieldType.X),
                        listOf(FieldType.O, FieldType.O, FieldType.NONE)
                )

        Assert.assertEquals("O should win if it occupies all 3 fields in diagonal.",
                FieldType.O, presenter.checkWinner(gameBoardStateOWinFake))
    }

    @Test
    fun givenNoWinner_whenGetWinner_thenReturnsNone() {
        val gameBoardStateNoneWinFake =
                listOf(
                        listOf(FieldType.NONE, FieldType.X, FieldType.X),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        Assert.assertEquals("NONE should win if no winning conditions are met.",
                FieldType.NONE, presenter.checkWinner(gameBoardStateNoneWinFake))
    }

    @Test
    fun givenAllFieldsTakenAndNoWinner_whenIsDraw_thenReturnsTrue() {
        val gameBoardStateDrawFake =
                listOf(
                        listOf(FieldType.O, FieldType.X, FieldType.O),
                        listOf(FieldType.O, FieldType.X, FieldType.O),
                        listOf(FieldType.X, FieldType.O, FieldType.X)
                )
        Assert.assertTrue(presenter.isDraw(gameBoardStateDrawFake))

        val gameBoardStateNotDrawFake =
                listOf(
                        listOf(FieldType.O, FieldType.X, FieldType.O),
                        listOf(FieldType.O, FieldType.X, FieldType.O),
                        listOf(FieldType.O, FieldType.O, FieldType.X)
                )
        Assert.assertFalse(presenter.isDraw(gameBoardStateNotDrawFake))
    }

    @Test
    fun whenReloadPlayground_thenCleanGameBoardShouldNotChange() {
        presenter.reloadGameBoard(
                listOf(
                        listOf(FieldType.X, FieldType.X, FieldType.O),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        )

        val expected = listOf(
                listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE),
                listOf(FieldType.NONE, FieldType.NONE, FieldType.NONE)
        )
        Assert.assertEquals(expected, presenter.cleanGameBoard)
    }

    @Test
    fun givenWinner_whenClickField_thenShowWinnerInView() {
        presenter.reloadGameBoard(
                listOf(
                        listOf(FieldType.X, FieldType.X, FieldType.NONE),
                        listOf(FieldType.O, FieldType.NONE, FieldType.NONE),
                        listOf(FieldType.X, FieldType.NONE, FieldType.NONE)
                )
        )
        presenter.currentPlayerState = FieldType.X

        presenter.clickField(2)

        verify(viewMock, times(0)).showDraw()
        verify(viewMock, times(1)).showWinner(FieldType.X.type)
    }

    @Test
    fun givenDraw_whenClickField_thenShowDrawInView() {
        presenter.reloadGameBoard(
                listOf(
                        listOf(FieldType.NONE, FieldType.X, FieldType.O),
                        listOf(FieldType.O, FieldType.O, FieldType.X),
                        listOf(FieldType.X, FieldType.O, FieldType.X)
                )
        )
        presenter.currentPlayerState = FieldType.X

        presenter.clickField(0)

        verify(viewMock, times(1)).showDraw()
        verify(viewMock, times(0)).showWinner(any())
    }

    @Test
    fun givenNotFinished_whenClickField_thenNotInteractWithView() {
        presenter.reloadGameBoard(
                listOf(
                        listOf(FieldType.NONE, FieldType.X, FieldType.O),
                        listOf(FieldType.NONE, FieldType.O, FieldType.X),
                        listOf(FieldType.X, FieldType.O, FieldType.X)
                )
        )
        presenter.currentPlayerState = FieldType.X

        presenter.clickField(0)

        verify(viewMock, times(0)).showDraw()
        verify(viewMock, times(0)).showWinner(any())
    }
}
