package com.wafflestudio.tictactoe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.tictactoe.databinding.GameRecordItemBinding
import com.wafflestudio.tictactoe.databinding.LastBoardItemBinding
import com.wafflestudio.tictactoe.databinding.StartGameItemBinding
import com.wafflestudio.tictactoe.model.DrawerItem

class DrawerViewTypeAdapter(
    private val items: List<DrawerItem>,
    private val onBoardItemClick: (Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_START_BUTTON = 0
        private const val TYPE_GAME_RECORD = 1
        private const val TYPE_LAST_BOARD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DrawerItem.StartButtonItem -> TYPE_START_BUTTON
            is DrawerItem.GameRecordItem -> TYPE_GAME_RECORD
            is DrawerItem.LastBoardItem -> TYPE_LAST_BOARD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_START_BUTTON -> {
                val binding = StartGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StartGameViewHolder(binding)
            }
            TYPE_GAME_RECORD -> {
                val binding = GameRecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GameRecordViewHolder(binding)
            }
            TYPE_LAST_BOARD -> {
                val binding = LastBoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LastBoardViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid View Type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is StartGameViewHolder -> holder.bind { onBoardItemClick(0) }
            is GameRecordViewHolder -> holder.bind(items[position] as DrawerItem.GameRecordItem, onBoardItemClick)
            is LastBoardViewHolder -> holder.bind(items[position] as DrawerItem.LastBoardItem)
        }
    }


    class StartGameViewHolder(private val binding: StartGameItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(onStartGameClick: () -> Unit) {
            binding.startGameButton.setOnClickListener { onStartGameClick() }
        }
    }

    class GameRecordViewHolder(private val binding: GameRecordItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardItem: DrawerItem.GameRecordItem, onBoardItemClick: (Int) -> Unit) {
            binding.moveNumber.text = "${boardItem.moveNumber} 턴"

            val cellIds = arrayOf(
                binding.cell00, binding.cell01, binding.cell02, binding.cell03, binding.cell04,
                binding.cell10, binding.cell11, binding.cell12, binding.cell13, binding.cell14,
                binding.cell20, binding.cell21, binding.cell22, binding.cell23, binding.cell24,
                binding.cell30, binding.cell31, binding.cell32, binding.cell33, binding.cell34,
                binding.cell40, binding.cell41, binding.cell42, binding.cell43, binding.cell44,
            )

            for (i in boardItem.board.indices) {
                for (j in boardItem.board[i].indices) {
                    cellIds[i * 5 + j].text = boardItem.board[i][j]
                }
            }

            binding.gotoButton.setOnClickListener { onBoardItemClick(boardItem.moveNumber) }
        }
   }

    class LastBoardViewHolder(private val binding: LastBoardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardItem: DrawerItem.LastBoardItem) {
            binding.stateText.text = boardItem.gameStatus

            val cellIds = arrayOf(
                binding.cell00, binding.cell01, binding.cell02, binding.cell03, binding.cell04,
                binding.cell10, binding.cell11, binding.cell12, binding.cell13, binding.cell14,
                binding.cell20, binding.cell21, binding.cell22, binding.cell23, binding.cell24,
                binding.cell30, binding.cell31, binding.cell32, binding.cell33, binding.cell34,
                binding.cell40, binding.cell41, binding.cell42, binding.cell43, binding.cell44,
            )

            for (i in boardItem.board.indices) {
                for (j in boardItem.board[i].indices) {
                    cellIds[i * 5 + j].text = boardItem.board[i][j]
                }
            }
        }
    }


    override fun getItemCount(): Int = items.size

}
