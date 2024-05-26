package com.example.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.realtimecalorietracking.R
import com.example.realtimecalorietracking.databinding.FragmentCaloriesBinding
import com.example.realtimecalorietracking.databinding.FragmentHomeBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimecalorietracking.view.adapter.CaloriesItem
import com.example.realtimecalorietracking.view.adapter.CaloriesAdapter


class calories : Fragment() {

    private lateinit var binding: FragmentCaloriesBinding
    private lateinit var adapter: CaloriesAdapter
    private lateinit var calorieItemList: MutableList<CaloriesItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaloriesBinding.inflate(inflater)

        // Initialize data
        calorieItemList = mutableListOf(
            CaloriesItem("Ensalada", "200 Cal", "25-05-2024"),
            CaloriesItem("Pizza", "400 Cal", "24-05-2024")
            // Add more items as needed
        )

        adapter = CaloriesAdapter(calorieItemList)
        binding.recyclerViewCalories.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCalories.adapter = adapter

        return binding.root
    }


}