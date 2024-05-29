package com.app.realtimecalorietracking.model

import java.io.Serializable
import java.util.Date

data class Food(
    val food_id: Int,
    val user_id: Int,
    val date: Date,
    val name: String,
    val calories: Int
): Serializable
