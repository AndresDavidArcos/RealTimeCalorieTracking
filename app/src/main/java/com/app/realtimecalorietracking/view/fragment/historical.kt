package com.app.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.databinding.FragmentHistoricalBinding
import com.app.realtimecalorietracking.databinding.FragmentHomeBinding


class historical : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricalBinding.inflate(inflater)
        return binding.root
    }
}