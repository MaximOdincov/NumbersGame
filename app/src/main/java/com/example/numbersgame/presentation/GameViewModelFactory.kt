package com.example.numbersgame.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.numbersgame.domain.entity.Level

class GameViewModelFactory(
    private val level: Level
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(level) as T
        }
        throw RuntimeException("Unknown view model class $modelClass")
    }
}