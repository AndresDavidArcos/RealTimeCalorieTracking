package com.app.realtimecalorietracking.model

data class FoodResponse(
    val parsed: List<ParsedFood>
)

data class ParsedFood(
    val food: ApiFood
)

data class ApiFood(
    val label: String,
    val nutrients: Nutrients
)

data class Nutrients(
    val ENERC_KCAL: Float
)
