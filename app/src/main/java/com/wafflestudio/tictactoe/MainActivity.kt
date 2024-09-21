package com.wafflestudio.tictactoe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.wafflestudio.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TicTacToeViewModel
    private lateinit var buttons: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TicTacToeViewModel::class.java]

        setupDrawer()
        setupGame()
        observeViewModel()
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
                viewModel.onCellClicked(index / 3, index % 3)
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetGame()
        }
    }

    private fun observeViewModel() {
        viewModel.board.observe(this) { board ->
            board.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, cell ->
                    buttons[rowIndex * 3 + colIndex].text = cell
                }
            }
        }

        viewModel.gameStatus.observe(this) { status ->
            binding.gameStatusText.text = status
        }

        viewModel.isGameOver.observe(this) { isGameOver ->
            buttons.forEach { it.isEnabled = !isGameOver }
        }

        viewModel.resetButtonText.observe(this) { text ->
            binding.resetButton.text = text
        }
    }
}