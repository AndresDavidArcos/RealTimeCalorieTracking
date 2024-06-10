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
import androidx.navigation.fragment.findNavController
import com.app.realtimecalorietracking.databinding.FragmentCaloriesBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.realtimecalorietracking.MainActivity
import com.app.realtimecalorietracking.R
import com.app.realtimecalorietracking.adapter.CaloriesItem
import com.app.realtimecalorietracking.adapter.CaloriesAdapter
import com.app.realtimecalorietracking.view.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

@AndroidEntryPoint
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
            CaloriesItem("Spaguettis", "300 Cal", "10-06-2024"),
            CaloriesItem("Helado", "100 Cal", "10-06-2024"),
            CaloriesItem("Ensalada", "200 Cal", "10-06-2024"),
            CaloriesItem("Pizza", "400 Cal", "10-06-2024"),
            CaloriesItem("Sushi", "300 Cal", "10-06-2024"),
            CaloriesItem("Hamburguesa", "500 Cal", "10-06-2024")
            // Add more items as needed
        )

        // Filter the list
        val filteredList =
            filterRecentItems(calorieItemList, 0) // days = cantidad de dias previos a mostrarse

        adapter = CaloriesAdapter(filteredList)
        binding.recyclerViewCalories.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewCalories.adapter = adapter

        // Calculate and display total calories for today
        binding.tvTotalCalories.text =
            "Total de calorías hoy: ${calculateCaloriesForToday(filteredList)}"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun filterRecentItems(items: List<CaloriesItem>, days: Int): List<CaloriesItem> {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        val thresholdDate = Calendar.getInstance().apply {
            time = currentDate
            add(Calendar.DAY_OF_YEAR, -(days + 1))
        }.time

        return items.filter {
            val itemDate = sdf.parse(it.fecha)
            itemDate != null && !itemDate.before(thresholdDate)
        }
    }

    fun calculateCaloriesForToday(items: List<CaloriesItem>): Int {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val regex =
            Regex("[^0-9]+") // Expresión regular para eliminar caracteres que no sean números

        return items.filter { it.fecha == currentDate }
            .sumBy {
                val calories = it.calorias.replace(regex, "") // Eliminar caracteres no numéricos
                calories.toIntOrNull() ?: 0
            }
    }
}