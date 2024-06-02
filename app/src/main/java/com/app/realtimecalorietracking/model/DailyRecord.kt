package com.app.realtimecalorietracking.model

import java.io.Serializable
import java.util.Date

data class DailyRecord(
    val date: Date,
    val foods: List<Food>,
    val goal: Goals?
): Serializable
