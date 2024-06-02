package com.app.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.realtimecalorietracking.databinding.FragmentHistoricalBinding
import com.app.realtimecalorietracking.model.DailyRecord
import com.app.realtimecalorietracking.model.Food
import com.app.realtimecalorietracking.model.Goals
import com.app.realtimecalorietracking.adapter.HistoricalAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class historical : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding

    private val foodsList: List<Food> = listOf(
        // Dummy data
        Food(1, 1, Date(2023, 5, 25), "Apple", 100),
        Food(2, 1, Date(2023, 5, 25), "Sandwich", 300),
        Food(3, 1, Date(2023, 5, 26), "Orange Juice", 150)
    )

    private val goalsList: List<Goals> = listOf(
        // Dummy data
        Goals(1, 1, Date(2023, 5, 25), 2000),
        Goals(2, 1, Date(2023, 5, 26), 2200)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoricalBinding.inflate(inflater, container, false)

        val dailyRecords = processDailyRecords(foodsList, goalsList)
        val adapter = HistoricalAdapter(dailyRecords) { record ->
            showFoodDetails(record)
        }

        binding.recyclerViewHistorical.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHistorical.adapter = adapter

        return binding.root
    }

    fun processDailyRecords(foods: List<Food>, goals: List<Goals>): List<DailyRecord> {
        val groupedFoods = foods.groupBy { it.date }
        val dailyRecords = mutableListOf<DailyRecord>()

        groupedFoods.forEach { (date, foodsList) ->
            val closestGoal = goals.minByOrNull { kotlin.math.abs(it.date.time - date.time) }
            dailyRecords.add(DailyRecord(date, foodsList, closestGoal))
        }

        return dailyRecords
    }

    private fun showFoodDetails(record: DailyRecord) {
        val foodDetails = record.foods.joinToString("\n") { "${it.name} - ${it.calories} kcal" }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detalles del registro de ${record.date}")
            .setMessage(foodDetails)
            .setPositiveButton("OK", null)
            .show()
    }
}
