package com.example.numbersgame.data

import android.service.notification.NotificationListenerService.RankingMap
import com.example.numbersgame.domain.entity.GameSettings
import com.example.numbersgame.domain.entity.Level
import com.example.numbersgame.domain.entity.Question
import com.example.numbersgame.domain.repository.GameRepository
import kotlin.random.Random

object GameRepositoryImpl: GameRepository {
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(1, maxSumValue+1)
        val visibleNumber = Random.nextInt(0, sum)
        val options = HashSet<Int>().apply {
            val rightAnswer = sum-visibleNumber
            add(rightAnswer)
            while(size < countOfOptions){
                add(Random.nextInt(0, sum+10))
            }
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level){
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                    8
                )
            }
            Level.EASY -> {
                GameSettings(
                    20,
                    10,
                    50,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    100,
                    15,
                    70,
                    60
                )
            }
            Level.HARD -> {
                GameSettings(
                    100,
                    20 ,
                    100,
                    30
                )
            }
        }
    }
}