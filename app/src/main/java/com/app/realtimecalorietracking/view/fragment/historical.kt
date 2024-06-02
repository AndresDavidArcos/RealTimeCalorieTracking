package com.app.realtimecalorietracking.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.realtimecalorietracking.databinding.FragmentHistoricalBinding
import com.app.realtimecalorietracking.model.Goals
import com.app.realtimecalorietracking.adapter.HistoricalAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class historical : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding

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

        val adapter = HistoricalAdapter(goalsList) { goal ->
            showFoodDetails(goal)
        }

        binding.recyclerViewHistorical.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHistorical.adapter = adapter

        return binding.root
    }

    private fun showFoodDetails(goal: Goals) {
        // Consumir lista de comidas
        val foodList = listOf(
            // Dummy data
            "Apple - 100 kcal",
            "Sandwich - 300 kcal",
            "Orange Juice - 150 kcal"
        )

        val foodDetails = foodList.joinToString("\n")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Food Details on ${goal.date}")
            .setMessage(foodDetails)
            .setPositiveButton("OK", null)
            .show()
    }
}
