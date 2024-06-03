package com.app.realtimecalorietracking.model

import java.io.Serializable
import java.util.Date

data class Food(
    val user_id: String,
    val date: Date,
    val name: String,
    val calories: Int
): Serializable
