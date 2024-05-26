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
import java.text.SimpleDateFormat
import java.util.*


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
            CaloriesItem("Spaguettis", "300 Cal", "26-05-2024"),
            CaloriesItem("Helado", "100 Cal", "26-05-2024"),
            CaloriesItem("Ensalada", "200 Cal", "25-05-2024"),
            CaloriesItem("Pizza", "400 Cal", "24-05-2024"),
            CaloriesItem("Sushi", "300 Cal", "20-05-2024"),
            CaloriesItem("Hamburguesa", "500 Cal", "19-05-2024")
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

    private fun calculateCaloriesForToday(items: List<CaloriesItem>): Int {
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