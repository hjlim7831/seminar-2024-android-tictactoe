package com.wafflestudio.tictactoe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wafflestudio.tictactoe.model.GameRecord

class TicTacToeViewModel: ViewModel() {
    private val _boardHistory = MutableLiveData(
        mutableListOf(List(3) { List(3) {""} })
    )
    val boardHistory: MutableLiveData<MutableList<List<List<String>>>> = _boardHistory

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _gameStatus = MutableLiveData("게임 시작!")
    val gameStatus: LiveData<String> = _gameStatus

    private val _isGameOver = MutableLiveData(false)
    val isGameOver: LiveData<Boolean> = _isGameOver

    private val _resetButtonText = MutableLiveData("초기화")
    val resetButtonText: LiveData<String> = _resetButtonText

    private val _gameRecords = MutableLiveData<List<GameRecord>>(mutableListOf())
    val gameRecords: LiveData<List<GameRecord>> = _gameRecords

    fun onCellClicked(row: Int, col: Int) {
        val currentBoard = getCurrentBoard()
        val currentPlayer = getCurrentPlayer()
        Log.d("tracking", "currentPlayer: $currentPlayer")

        if (_isGameOver.value == true || currentBoard[row][col].isNotEmpty()) return

        val newBoard = currentBoard.mapIndexed { r, rowList ->
            rowList.mapIndexed { c, cell ->
                if (r == row && c == col) currentPlayer else cell
            }
        }
        Log.d("tracking", "newBoard: $newBoard")

        _boardHistory.value = _boardHistory.value?.apply {
            add(newBoard)
        }

        _currentIndex.value = (_currentIndex.value ?: 0) + 1

        if (checkWin(row, col)) {
            _gameStatus.value = "$currentPlayer 승리!"
            endGame()
        } else if (isBoardFull()) {
            _gameStatus.value = "무승부!"
            endGame()
        } else {
            _gameStatus.value = "$currentPlayer 의 차례입니다"
        }
    }

    fun getCurrentBoard(): List<List<String>> {
        return boardHistory.value?.getOrNull(currentIndex.value ?: 0)
            ?: List(3) { List(3) {""} }
    }

    private fun getCurrentPlayer(): String {
        return if (_currentIndex.value!! % 2 == 0) "O" else "X"
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val board = getCurrentBoard()
        val player = board[row][col]
        if (player.isEmpty()) return false

        // check row
        if (board[row].all { it == player }) return true

        // check col
        if (board.all { it[col] == player }) return true

        // check diagonals
        if (row == col && board.indices.all { board[it][it] == player }) return true
        if (row + col == 2 && board.indices.all { board[it][2 - it] == player }) return true

        return false
    }

    private fun isBoardFull(): Boolean {
        val currentBoard = getCurrentBoard()
        return currentBoard.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun endGame() {
        _isGameOver.value = true
        _resetButtonText.value = "한판 더"
    }

    fun resetGame() {
        _boardHistory.value = mutableListOf(List(3) { List(3) {""} })
        _currentIndex.value = 0
        _gameStatus.value = "게임 시작!"
        _isGameOver.value = false
        _resetButtonText.value = "초기화"
    }
}
