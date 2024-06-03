package com.app.realtimecalorietracking.model;

import java.io.Serializable

data class User(
    val user_id: String,
    val full_name: String,
    val gender: String,
    val age: Int,
    val weight: Int,
    val height: Int,
    val activity: String
):Serializable
