package com.app.realtimecalorietracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimecalorietracking.databinding.ItemHistoricalRecordBinding
import com.app.realtimecalorietracking.model.Goals
import java.text.SimpleDateFormat
import java.util.*

class HistoricalAdapter(
    private val goalsList: List<Goals>,
    private val onItemClick: (Goals) -> Unit
) : RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder>() {

    inner class HistoricalViewHolder(private val binding: ItemHistoricalRecordBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goal: Goals) {
            binding.textViewDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(goal.date)
            binding.textViewCaloriesConsumed.text = "Calories Consumed: ${goal.calories}"
            binding.textViewCaloriesGoal.text = "Goal: ${goal.calories} kcal"

            binding.root.setOnClickListener {
                onItemClick(goal)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalViewHolder {
        val binding = ItemHistoricalRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoricalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoricalViewHolder, position: Int) {
        holder.bind(goalsList[position])
    }

    override fun getItemCount(): Int = goalsList.size
}
