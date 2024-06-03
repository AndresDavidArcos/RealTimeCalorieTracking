package com.app.realtimecalorietracking.model

import java.util.Date
import java.io.Serializable

data class Goals(
    val user_id: String,
    val date: Date,
    val calories: Int
):Serializable