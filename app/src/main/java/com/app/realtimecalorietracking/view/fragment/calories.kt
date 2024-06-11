package com.app.realtimecalorietracking.view.fragment

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.realtimecalorietracking.databinding.FragmentCaloriesBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.realtimecalorietracking.MainActivity
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.adapter.CaloriesAdapter
import com.app.realtimecalorietracking.model.Food
import com.app.realtimecalorietracking.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.app.realtimecalorietracking.viewmodel.LoginViewModel

@AndroidEntryPoint
class calories : Fragment() {

    private lateinit var binding: FragmentCaloriesBinding
    private lateinit var adapter: CaloriesAdapter
    private lateinit var calorieItemList: MutableList<Food>
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaloriesBinding.inflate(inflater)

        // Initialize data
        calorieItemList = viewModel.getTodayCaloriesItems()

        // Filter the list
        val filteredList =
            viewModel.filterRecentItems(calorieItemList, 0) // days = cantidad de dias previos a mostrarse

        adapter = CaloriesAdapter(filteredList)
        binding.recyclerViewCalories.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCalories.adapter = adapter

        // Calculate and display total calories for today
        binding.tvTotalCalories.text =
            "Total de calorías hoy: ${viewModel.calculateCaloriesForToday(filteredList)}"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}