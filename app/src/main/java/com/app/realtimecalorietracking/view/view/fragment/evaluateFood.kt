package com.app.realtimecalorietracking.view.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentEvaluateFoodBinding
import com.app.realtimecalorietracking.databinding.FragmentHomeBinding


class evaluateFood : Fragment() {
    private lateinit var binding: FragmentEvaluateFoodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEvaluateFoodBinding.inflate(inflater)
        return binding.root
    }

}