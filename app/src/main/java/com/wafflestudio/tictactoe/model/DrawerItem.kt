package com.wafflestudio.tictactoe.model

sealed class DrawerItem {
    data object StartButtonItem: DrawerItem()
    data class GameRecordItem(val moveNumber: Int, val board: List<List<String>>): DrawerItem()
    data class LastBoardItem(val board: List<List<String>>, val gameStatus: String): DrawerItem()
}