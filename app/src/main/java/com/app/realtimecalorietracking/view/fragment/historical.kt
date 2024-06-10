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
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class historical : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoricalBinding.inflate(inflater, container, false)

        fetchFoodsAndGoals()

        return binding.root
    }

    private fun fetchFoodsAndGoals() {
        val foodsList = mutableListOf<Food>()
        val goalsList = mutableListOf<Goals>()

        db.collection("foods")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val food = document.toObject(Food::class.java)
                    foodsList.add(food)
                }
                fetchGoals(foodsList, goalsList)
            }
            .addOnFailureListener { exception ->
                handleFirebaseError(exception, "Error al obtener los datos de alimentos")
            }
    }

    private fun fetchGoals(foodsList: List<Food>, goalsList: MutableList<Goals>) {
        db.collection("goals")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val goal = document.toObject(Goals::class.java)
                    goalsList.add(goal)
                }
                setupRecyclerView(foodsList, goalsList)
            }
            .addOnFailureListener { exception ->
                handleFirebaseError(exception, "Error al obtener los datos de metas")
            }
    }
    private fun handleFirebaseError(exception: Exception, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("$message: ${exception.message}")
            .setPositiveButton("OK", null)
            .show()
    }
    private fun setupRecyclerView(foodsList: List<Food>, goalsList: List<Goals>) {
        val dailyRecords = processDailyRecords(foodsList, goalsList)
        val adapter = HistoricalAdapter(dailyRecords) { record ->
            showFoodDetails(record)
        }

        binding.recyclerViewHistorical.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHistorical.adapter = adapter
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
            .setTitle("Details of record in ${record.date}")
            .setMessage(foodDetails)
            .setPositiveButton("OK", null)
            .show()
    }
}
