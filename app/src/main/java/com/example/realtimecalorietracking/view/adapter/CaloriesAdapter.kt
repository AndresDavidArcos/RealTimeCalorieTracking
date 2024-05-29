package com.example.realtimecalorietracking.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realtimecalorietracking.databinding.CardviewCaloriesBinding

data class CaloriesItem(val nombrePlato: String, val calorias: String, val fecha: String)

class CaloriesAdapter(private val calorieItems: List<CaloriesItem>) :
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
        fun bind(item: CaloriesItem) {
            binding.tvNombrePlato.text = item.nombrePlato
            binding.tvCalorias.text = item.calorias
            binding.tvFecha.text = item.fecha
        }
    }
}