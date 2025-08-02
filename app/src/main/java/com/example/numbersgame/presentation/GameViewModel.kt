package com.example.numbersgame.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numbersgame.data.GameRepositoryImpl
import com.example.numbersgame.domain.entity.GameResult
import com.example.numbersgame.domain.entity.GameSettings
import com.example.numbersgame.domain.entity.Level
import com.example.numbersgame.domain.entity.Question
import com.example.numbersgame.domain.usecases.GenerateQuestionUseCase
import com.example.numbersgame.domain.usecases.GetGameSettingsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val level: Level
): ViewModel() {
    private val repository = GameRepositoryImpl
    private lateinit var gameSettings: GameSettings

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var countOfQuestion = 0
    private var countOfAnswers = 0

    private var _timer = MutableLiveData<Int>()
    val timer: LiveData<Int>
        get(){
            return _timer
        }
    private var timerJob: Job? = null


    private var _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get(){
            return _gameResult
        }

    private var _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question>
        get(){
            return _currentQuestion
        }

    private var _countOfRightAnswers = MutableLiveData<Int>()
    val countOfRightAnswers: LiveData<Int>
        get(){
            return _countOfRightAnswers
        }

    private var _countOfMinAnswers = MutableLiveData<Int>()
    val countOfMinAnswers: LiveData<Int>
        get(){
            return _countOfMinAnswers
        }

    fun startTimer(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            if(gameSettings.gameTimeInSeconds >= 3600){
                throw RuntimeException("Time can't be more 59 minutes 59 seconds")
            }
            _timer.postValue(gameSettings.gameTimeInSeconds)
            while (_timer.value != 0){
                delay(1000)
                _timer.postValue(_timer.value?.minus(1))
                Log.d("TIMER COROUTINE", "I'm alive")
            }
            calculateResult()

        }
    }

    init{
        gameSettings = getGameSettingsUseCase.invoke(level)
        _countOfMinAnswers.value = gameSettings.minCountOfRightAnswers
        _countOfRightAnswers.value = 0
    }

    fun generateNewQuestion(){
        viewModelScope.launch(Dispatchers.Default)
        {
            _currentQuestion.postValue(generateQuestionUseCase.invoke(gameSettings.maxSumValue))
            countOfQuestion++
        }
    }

    fun addResult(answer: Int){
        val question = _currentQuestion.value ?: throw RuntimeException("Question is null")
        val correctAnswer = question.sum - question.visibleNumber
        countOfAnswers++
        if(answer == correctAnswer) _countOfRightAnswers.postValue(_countOfRightAnswers.value?.plus(1))
        generateNewQuestion()
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }

    private fun calculateResult(){
        val rightAnswers = _countOfRightAnswers.value ?: throw RuntimeException("count of right answers is null")
        val minAnswers = _countOfMinAnswers.value ?: throw RuntimeException("count of minimal right answers is null")
        var isWin = false
        if(rightAnswers>minAnswers){
            isWin = true
        }
        val result = GameResult(isWin, countOfQuestion, countOfAnswers, gameSettings)
        _gameResult.value = result
    }

    companion object{
        fun convertTimerValue(seconds: Int): String{
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%02d:%02d", minutes, remainingSeconds)
        }
    }
}