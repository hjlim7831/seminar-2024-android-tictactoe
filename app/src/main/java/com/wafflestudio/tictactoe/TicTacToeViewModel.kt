package com.wafflestudio.tictactoe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
        updateGameStatus()
    }

    private fun updateGameStatus() {
        val currentBoard = getCurrentBoard()
        // 승리 여부 체크
        if (checkWin(currentBoard)) {
            _gameStatus.value = "${getOpponentPlayer()}가 승리했습니다!"
            _isGameOver.value = true
            return
        }
        if (isBoardFull()) {
            _gameStatus.value = "무승부!"
            _isGameOver.value = true
            return
        }
        // 차례 변경: 현재 차례가 "O"면 "X", "X"면 "O"로 전환
        _gameStatus.value = "${getCurrentPlayer()}의 차례입니다"
        _isGameOver.value = false
    }

    fun getCurrentBoard(): List<List<String>> {
        return boardHistory.value?.getOrNull(currentIndex.value ?: 0)
            ?: List(3) { List(3) {""} }
    }

    private fun getCurrentPlayer(): String {
        return if (_currentIndex.value!! % 2 == 0) "O" else "X"
    }

    private fun getOpponentPlayer(): String {
        return if (getCurrentPlayer() == "O") "X" else "O"
    }

    private fun checkWin(board: List<List<String>>): Boolean {

        // check row
        for (row in board) {
            if (row[0].isNotEmpty() && row.all { it == row[0] }) {
                return true
            }
        }
        // check col
        for (col in board.indices) {
            if (board[0][col].isNotEmpty() && board.all { it[col] == board[0][col] }) {
                return true
            }
        }

        // check main diagonal
        if (board[0][0].isNotEmpty() && board.indices.all { board[it][it] == board[0][0] }) {
            return true
        }

        // Check anti-diagonal
        if (board[0][2].isNotEmpty() && board.indices.all { board[it][2 - it] == board[0][2] }) {
            return true
        }


        return false
    }

    private fun isBoardFull(): Boolean {
        val currentBoard = getCurrentBoard()
        return currentBoard.all { row -> row.all { it.isNotEmpty() } }
    }

    fun resetGame() {
        _boardHistory.value = mutableListOf(List(3) { List(3) {""} })
        _currentIndex.value = 0
        _gameStatus.value = "게임 시작!"
        _isGameOver.value = false
    }
    
    fun getResetButtonText():String {
        return if (_isGameOver.value == false) "초기화" else "한판 더"
    }

    fun goToMove(position: Int) {
        if (position >= 0 && position < (_boardHistory.value?.size ?:0)) {
            _currentIndex.value = position
            _boardHistory.value = _boardHistory.value?.take(position + 1)?.toMutableList()
            updateGameStatus()
        }
    }
}
