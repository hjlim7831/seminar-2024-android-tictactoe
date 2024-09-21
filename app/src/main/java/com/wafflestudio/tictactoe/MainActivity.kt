package com.wafflestudio.tictactoe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.wafflestudio.tictactoe.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentPlayer = "X"
    private val board = Array(3) { Array(3) {""} }
    private lateinit var buttons: List<Button>
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawer()
        setupGame()
    }

    private fun setupDrawer() {
        binding.drawerButton.setOnClickListener {
            if (binding.main.isDrawerOpen(GravityCompat.START)) {
                binding.main.closeDrawer(GravityCompat.START)
            } else {
                binding.main.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupGame() {
        buttons = listOf(
            binding.button00, binding.button01, binding.button02,
            binding.button10, binding.button11, binding.button12,
            binding.button20, binding.button21, binding.button22
        )
        buttons.forEachIndexed { index, button ->

            button.setOnClickListener {
                val row = index / 3
                val col = index % 3
                if (board[row][col].isEmpty()) {
                    board[row][col] = currentPlayer
                    button.text = currentPlayer
                    if (checkWin(row, col)) {
                        binding.gameStatusText.text = "$currentPlayer 승리!"
                        endGame()
                    } else if (isBoardFull()) {
                        binding.gameStatusText.text = "무승부!"
                        endGame()
                    } else {
                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                        binding.gameStatusText.text = "$currentPlayer 의 차례입니다"
                    }
                }
            }
        }

        binding.resetButton.setOnClickListener {
            resetGame()
        }
        resetGame()
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        return (board[row][0] == board[row][1] && board[row][1] == board[row][2]) ||
                (board[0][col] == board[1][col] && board[1][col] == board[2][col]) ||
                (row == col && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (row + col == 2 && board[0][2] == board[1][1] && board[1][1] == board[2][0])
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    private fun endGame() {
        isGameOver = true
        disableButtons()
        binding.resetButton.text = "한판 더"
    }


    private fun disableButtons() {
        buttons.forEach { it.isEnabled = false }
    }

    private fun resetGame() {
        board.forEach { row -> row.fill("") }
        buttons.forEach {
            it.text = ""
            it.isEnabled = true
        }
        currentPlayer = "X"
        binding.gameStatusText.text = "게임 시작!"
        binding.resetButton.text = "초기화"
    }



}