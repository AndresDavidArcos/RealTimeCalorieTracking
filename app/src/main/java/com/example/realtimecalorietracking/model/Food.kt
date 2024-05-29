package com.example.realtimecalorietracking.model
import java.util.Date

data class FoodRegistered(
    val foodId: Int,
    val userId: Int,
    val foodName: String,
    val calories: Int,
    val dateRegistered: Date
)