package com.example.numbersgame.domain.usecases

import com.example.numbersgame.domain.entity.GameSettings
import com.example.numbersgame.domain.entity.Level
import com.example.numbersgame.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository
) {
    operator fun invoke(level: Level): GameSettings{
       return repository.getGameSettings(level)
    }
}