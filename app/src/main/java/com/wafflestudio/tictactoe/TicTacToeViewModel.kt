package com.wafflestudio.tictactoe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TicTacToeViewModel: ViewModel() {
    private val _boardHistory = MutableLiveData(
        mutableListOf(List(5) { List(5) {""} })
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
            ?: List(5) { List(5) {""} }
    }

    private fun getCurrentPlayer(): String {
        return if (_currentIndex.value!! % 2 == 0) "O" else "X"
    }

    private fun getOpponentPlayer(): String {
        return if (getCurrentPlayer() == "O") "X" else "O"
    }

    private fun checkWin(board: List<List<String>>): Boolean {
        val n = board.size
        val directions = listOf(
            Pair(1, 0),  // 가로
            Pair(0, 1),  // 세로
            Pair(1, 1),  // 대각선 (좌상단 -> 우하단)
            Pair(1, -1)  // 대각선 (좌하단 -> 우상단)
        )

        for (row in 0 until n) {
            for (col in 0 until n) {
                if (board[row][col].isNotEmpty()) {
                    for ((dx, dy) in directions) {
                        if (checkDirection(board, row, col, dx, dy)) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    private fun checkDirection(board: List<List<String>>, row: Int, col: Int, dx: Int, dy: Int): Boolean {
        val player = board[row][col]
        val n = board.size

        for (i in 0 until 5) {
            val newRow = row + i * dx
            val newCol = col + i * dy
            if (newRow !in 0 until n || newCol !in 0 until n || board[newRow][newCol] != player) {
                return false
            }
        }
        return true
    }


    private fun isBoardFull(): Boolean {
        val currentBoard = getCurrentBoard()
        return currentBoard.all { row -> row.all { it.isNotEmpty() } }
    }

    fun resetGame() {
        _boardHistory.value = mutableListOf(List(5) { List(5) {""} })
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
