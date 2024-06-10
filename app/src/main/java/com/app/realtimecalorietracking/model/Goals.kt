package com.app.realtimecalorietracking.model

import java.util.Date
import java.io.Serializable

data class Goals(
    var user_id: String = "",
    var date: Date = Date(),
    var calories: Int = 0
)