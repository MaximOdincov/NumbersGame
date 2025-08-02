package com.example.numbersgame.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.numbersgame.R
import com.example.numbersgame.databinding.FragmentGameBinding
import com.example.numbersgame.databinding.FragmentWelcomeBinding
import com.example.numbersgame.domain.entity.GameResult
import com.example.numbersgame.domain.entity.GameSettings
import com.example.numbersgame.domain.entity.Level
import com.example.numbersgame.domain.entity.Question
import com.google.android.material.textfield.TextInputLayout.LengthCounter

class GameFragment : Fragment() {

    private val binding by viewBinding(FragmentGameBinding::bind)
    private lateinit var level: Level
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, GameViewModelFactory(level))[GameViewModel::class.java]
    }
    private var minCountOfRightAnswer = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startGame()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun parseArgs(){
        (requireArguments().getParcelable(KEY_LEVEL, Level::class.java) as Level)?.let {
            level = it
        }
    }

    private fun launchToGameFinishedScreen(gameResult: GameResult){
        findNavController().navigate(R.id.action_gameFragment_to_gameFinishedFragment, GameFinishedFragment.newInstance(gameResult))
    }

    private fun startTimer(){
        viewModel.timer.observe(viewLifecycleOwner){
            binding.timerTextView.text = GameViewModel.convertTimerValue(it)
        }
        viewModel.startTimer()
    }

    private fun startGame(){
        binding.progressBar.progress = 0
        viewModel.gameResult.observe(viewLifecycleOwner){
            if(it != null){
                finishGame(it)
            }
        }

        viewModel.currentQuestion.observe(viewLifecycleOwner){
            updateQuestionViews(it)
        }

        viewModel.countOfMinAnswers.observe(viewLifecycleOwner){
            minCountOfRightAnswer = it
        }

        viewModel.countOfRightAnswers.observe(viewLifecycleOwner){
            binding.answersProgressTextView.text = getString(R.string.right_answers).format(it, minCountOfRightAnswer)
            binding.progressBar.progress = (it.toDouble()/minCountOfRightAnswer*100).toInt()
        }
        viewModel.generateNewQuestion()
        startTimer()
    }

    @SuppressLint("SetTextI18n")
    private fun updateQuestionViews(newQuestion: Question){
        with(binding){
            sumTextView.text = newQuestion.sum.toString()
            leftNumberTextView.text = newQuestion.visibleNumber.toString()
            val options = listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6)
            for((i, option) in options.withIndex()){
                option.text = newQuestion.options[i].toString()
                option.setOnClickListener{
                    viewModel.addResult(option.text.toString().toInt())
                }
            }
        }

    }

    private fun finishGame(gameResult: GameResult){
        launchToGameFinishedScreen(gameResult)
    }

    override fun onDestroyView() {
        viewModelStore.clear()
        super.onDestroyView()
    }

    companion object{
        private const val KEY_LEVEL = "level"

        fun newInstance(level:Level): Bundle{
            return Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
            }
        }
    }
}