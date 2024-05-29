package com.app.realtimecalorietracking.model

import java.sql.Date
import java.io.Serializable

data class Goals(
    val goal_id: Int,
    val user_id: Int,
    val date: Date,
    val calories: Int
):Serializable