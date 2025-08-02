package com.example.numbersgame.presentation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.collection.intIntMapOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.numbersgame.R
import com.example.numbersgame.databinding.FragmentGameBinding
import com.example.numbersgame.databinding.FragmentGameFinishedBinding
import com.example.numbersgame.databinding.FragmentWelcomeBinding
import com.example.numbersgame.domain.entity.GameResult
import com.example.numbersgame.domain.entity.GameSettings

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult
    private val binding by viewBinding(FragmentGameFinishedBinding::bind)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parseArguments()
        return inflater.inflate(R.layout.fragment_game_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun parseArguments(){
        requireArguments().getParcelable(GAME_RESULT, GameResult::class.java)?.let {
            gameResult = it
        }
    }

    private fun setViews(){
        var image = 0
        if(gameResult.isWin) image = R.drawable.pass
        else image = R.drawable.fail
        binding.resultImg.setImageResource(image)
        binding.scoreAnswersTextView.text =
            getString(R.string.score_answers).format(gameResult.countOfRightAnswers.toString())
        binding.requiredAnswersTextView.text =
            getString(R.string.required_score).format(gameResult.gameSettings.minCountOfRightAnswers.toString())
        binding.scorePercentageTextView.text =
            getString(R.string.score_percentage).format((gameResult.countOfRightAnswers.toDouble()/gameResult.countOfQuestion*100).toInt().toString())
        binding.requiredPercentageTextView.text =
            getString(R.string.required_percentage).format(gameResult.gameSettings.minPercentsOfRightAnswers.toString())

        binding.buttonRetry.setOnClickListener{launchRetry()}
    }

    private fun launchRetry(){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ChooseLevelFragment.newInstance())
            .commit()
    }

    companion object{
        private const val GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment{
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}