package com.app.realtimecalorietracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimecalorietracking.databinding.ItemHistoricalRecordBinding
import com.app.realtimecalorietracking.model.DailyRecord
import java.text.SimpleDateFormat
import java.util.*

class HistoricalAdapter(
    private val dailyRecords: List<DailyRecord>,
    private val onItemClick: (DailyRecord) -> Unit
) : RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder>() {

    inner class HistoricalViewHolder(private val binding: ItemHistoricalRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: DailyRecord) {
            binding.textViewDate.text ="Fecha: "+ SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(record.date)
            val totalCaloriesConsumed = record.foods.sumOf { it.calories }
            binding.textViewCaloriesConsumed.text = "Consumed calories: $totalCaloriesConsumed"
            binding.textViewCaloriesGoal.text = "Goal: ${record.goal?.calories ?: "N/A"} kcal"

            binding.root.setOnClickListener {
                onItemClick(record)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalViewHolder {
        val binding = ItemHistoricalRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        holder.bind(dailyRecords[position])
    }

    override fun getItemCount(): Int = dailyRecords.size
}
