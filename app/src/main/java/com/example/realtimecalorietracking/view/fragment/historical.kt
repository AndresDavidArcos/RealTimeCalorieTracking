package com.example.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.realtimecalorietracking.R
import com.example.realtimecalorietracking.databinding.FragmentHistoricalBinding
import com.example.realtimecalorietracking.databinding.FragmentHomeBinding


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