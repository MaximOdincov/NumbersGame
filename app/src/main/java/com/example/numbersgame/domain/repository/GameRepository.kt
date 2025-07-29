package com.example.numbersgame.domain.repository

import com.example.numbersgame.domain.entity.GameSettings
import com.example.numbersgame.domain.entity.Level
import com.example.numbersgame.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question

    fun getGameSettings(level: Level): GameSettings
}