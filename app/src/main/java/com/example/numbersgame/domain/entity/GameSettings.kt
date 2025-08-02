package com.example.numbersgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minPercentsOfRightAnswers: Int,
    val gameTimeInSeconds: Int
): Parcelable