package com.example.realtimecalorietracking.view.fragment

import HistoricalAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimecalorietracking.databinding.FragmentHistoricalBinding
import java.util.Date
import com.example.realtimecalorietracking.model.Goal
import com.example.realtimecalorietracking.model.FoodRegistered

class historical : Fragment() {
    private lateinit var binding: FragmentHistoricalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historicalList = createHardcodedData()

        val adapter = HistoricalAdapter(historicalList)
        binding.recyclerViewHistorical.adapter = adapter
        binding.recyclerViewHistorical.layoutManager = LinearLayoutManager(context)
    }

    private fun createHardcodedData(): List<Pair<FoodRegistered, Goal>> {
        // Crear datos de prueba
        val foodRegistered1 = FoodRegistered(1, 1, "Comida 1", 600, Date())
        val foodRegistered2 = FoodRegistered(2, 1, "Comida 2", 800, Date())
        val goal = Goal(1, 1, Date(), 1500)

        return listOf(Pair(foodRegistered1, goal), Pair(foodRegistered2, goal))
    }
}
