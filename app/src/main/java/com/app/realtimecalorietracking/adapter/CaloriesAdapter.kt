package com.app.realtimecalorietracking.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.realtimecalorietracking.databinding.CardviewCaloriesBinding
import com.app.realtimecalorietracking.model.Food

class CaloriesAdapter(private val calorieItems: List<Food>) :
    RecyclerView.Adapter<CaloriesAdapter.CaloriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaloriesViewHolder {
        val binding = CardviewCaloriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaloriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaloriesViewHolder, position: Int) {
        val item = calorieItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return calorieItems.size
    }

    class CaloriesViewHolder(private val binding: CardviewCaloriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Food) {
            binding.tvNombrePlato.text = item.name
            binding.tvCalorias.text = item.calories.toString()
            binding.tvFecha.text = item.date.toString()
        }
    }
}