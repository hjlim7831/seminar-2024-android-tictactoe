package com.wafflestudio.tictactoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicTacToeViewModel: ViewModel() {
    private val _board = MutableLiveData(Array(3) { Array(3) { "" } })
    val board: LiveData<Array<Array<String>>> = _board

    private val _currentPlayer = MutableLiveData("X")
    val currentPlayer: LiveData<String> = _currentPlayer
    private val _gameStatus = MutableLiveData("게임 시작!")
    val gameStatus: LiveData<String> = _gameStatus

    private val _isGameOver = MutableLiveData(false)
    val isGameOver: LiveData<Boolean> = _isGameOver

    private val _resetButtonText = MutableLiveData("초기화")
    val resetButtonText: LiveData<String> = _resetButtonText

    fun onCellClicked(row: Int, col: Int) {

        if (_isGameOver.value == true || _board.value!![row][col].isNotEmpty()) return

        val currentBoard = _board.value!!
        currentBoard[row][col] = _currentPlayer.value!!
        _board.value = currentBoard

        if (checkWin(row, col)) {
            _gameStatus.value = "${_currentPlayer.value} 승리!"
            endGame()
        } else if (isBoardFull()) {
            _gameStatus.value = "무승부!"
            endGame()
        } else {
            _currentPlayer.value = if (_currentPlayer.value == "X") "O" else "X"
            _gameStatus.value = "${_currentPlayer.value}의 차례입니다"
        }
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val board = _board.value!!
        return (board[row][0] == board[row][1] && board[row][1] == board[row][2]) ||
                (board[0][col] == board[1][col] && board[1][col] == board[2][col]) ||
                (row == col && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (row + col == 2 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
    }

    private fun isBoardFull(): Boolean {
        return _board.value!!.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun endGame() {
        _isGameOver.value = true
        _resetButtonText.value = "한판 더"
    }

    fun resetGame() {
        _board.value = Array(3) { Array(3) { "" } }
        _currentPlayer.value = "X"
        _gameStatus.value = "게임 시작!"
        _isGameOver.value = false
        _resetButtonText.value = "초기화"
    }
}