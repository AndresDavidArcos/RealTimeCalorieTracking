package com.example.realtimecalorietracking.model
import java.util.Date

data class Goal(
    val goalId: Int,
    val userId: Int,
    val date: Date,
    val calories: Int
)