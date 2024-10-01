package com.wafflestudio.tictactoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.tictactoe.model.GameRecord

class GameAdapter(private val records: List<GameRecord>) :
    RecyclerView.Adapter<GameAdapter.GameRecordViewHolder>() {

        class GameRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val boardButtons: List<Button> = listOf(
                view.findViewById(R.id.button00),
                view.findViewById(R.id.button01),
                view.findViewById(R.id.button02),
                view.findViewById(R.id.button10),
                view.findViewById(R.id.button11),
                view.findViewById(R.id.button12),
                view.findViewById(R.id.button20),
                view.findViewById(R.id.button21),
                view.findViewById(R.id.button22)
            )
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameRecordViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main, parent, false)  // 새 보드 레이아웃을 inflate
            val gameBoard = view.findViewById<ConstraintLayout>(R.id.gameBoard)
            return GameRecordViewHolder(gameBoard)
        }

        override fun onBindViewHolder(holder: GameRecordViewHolder, position: Int) {
            val record = records[position]

            // 각 보드의 상태를 설정
            record.gameState.forEachIndexed { index, state ->
                holder.boardButtons[index].text = state  // 각 위치에 X, O, 빈칸 표시
            }
        }

        override fun getItemCount(): Int = records.size
        }
