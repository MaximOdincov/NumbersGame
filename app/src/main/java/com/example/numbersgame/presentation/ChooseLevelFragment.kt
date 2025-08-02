package com.example.numbersgame.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.numbersgame.R
import com.example.numbersgame.databinding.FragmentChooseLevelBinding
import com.example.numbersgame.databinding.FragmentWelcomeBinding
import com.example.numbersgame.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    private val binding by viewBinding(FragmentChooseLevelBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    private fun addListeners(){
        binding.lvl1Button.setOnClickListener{launchLevel(Level.TEST)}
        binding.lvl2Button.setOnClickListener{launchLevel(Level.EASY)}
        binding.lvl3Button.setOnClickListener{launchLevel(Level.NORMAL)}
        binding.lvl4Button.setOnClickListener{launchLevel(Level.HARD)}
    }

    private fun launchLevel(level: Level){
        findNavController().navigate(R.id.action_chooseLevelFragment_to_gameFragment, GameFragment.newInstance(level))
    }

    companion object{
        fun newInstance(): ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }
}