package com.wafflestudio.tictactoe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wafflestudio.tictactoe.databinding.ActivityMainBinding
import com.wafflestudio.tictactoe.model.DrawerItem

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
        setupDrawerRecyclerView()
        observeViewModel()
    }

    private fun setupDrawerRecyclerView() {
        binding.drawerItemRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        val drawerItems = mutableListOf<DrawerItem>()

        fun updateDrawerItems() {
            val history = viewModel.boardHistory.value ?: return
            val isGameOver = viewModel.isGameOver.value ?: false
            val gameStatus = viewModel.gameStatus.value ?: ""

            drawerItems.clear()
            drawerItems.add(DrawerItem.StartButtonItem)
            history.forEachIndexed { index, board ->
                if (index != 0) {
                    if (index == history.size - 1 && isGameOver) {
                        drawerItems.add(DrawerItem.LastBoardItem(board, gameStatus))
                    } else {
                        drawerItems.add(DrawerItem.GameRecordItem(index, board))
                    }
                }
            }

            binding.drawerItemRecyclerView.adapter = DrawerViewTypeAdapter(
                items = drawerItems,
                onBoardItemClick = { moveNumber ->
                    viewModel.goToMove(moveNumber)
                    binding.main.closeDrawer(GravityCompat.START)
                }
            )
        }

        viewModel.boardHistory.observe(this) { _ -> updateDrawerItems() }
        viewModel.isGameOver.observe(this) { _ -> updateDrawerItems() }
        viewModel.gameStatus.observe(this) { _ -> updateDrawerItems() }

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
            binding.button00, binding.button01, binding.button02, binding.button03, binding.button04,
            binding.button10, binding.button11, binding.button12, binding.button13, binding.button14,
            binding.button20, binding.button21, binding.button22, binding.button23, binding.button24,
            binding.button30, binding.button31, binding.button32, binding.button33, binding.button34,
            binding.button40, binding.button41, binding.button42, binding.button43, binding.button44
        )
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.onCellClicked(index / 5, index % 5)
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.resetGame()
        }
    }

    private fun observeViewModel() {
        viewModel.currentIndex.observe(this) { _ ->
            val currentBoard = viewModel.getCurrentBoard()
            currentBoard.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, cell ->
                    buttons[rowIndex * 5 + colIndex].text = cell
                }
            }
        }

        viewModel.gameStatus.observe(this) { status ->
            binding.gameStatusText.text = status
        }

        viewModel.isGameOver.observe(this) { isGameOver ->
            buttons.forEach { it.isEnabled = !isGameOver }
            binding.resetButton.text = viewModel.getResetButtonText()
        }
    }
}