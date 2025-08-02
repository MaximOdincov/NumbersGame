package com.example.numbersgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GameResult (
    val isWin: Boolean,
    val countOfQuestion: Int,
    val countOfRightAnswers: Int,
    val gameSettings: GameSettings
): Parcelable