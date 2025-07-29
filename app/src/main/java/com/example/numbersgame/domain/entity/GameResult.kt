package com.example.numbersgame.domain.entity

data class GameResult (
    val isWin: Boolean,
    val countOfQuestion: Int,
    val countOfRightAnswers: Int,
    val gameSettings: GameSettings
)