package com.app.realtimecalorietracking.model

import java.io.Serializable
import java.util.Date

data class Food(
    var user_id: String = "",
    var date: Date = Date(),
    var name: String = "",
    var calories: Int = 0
)
