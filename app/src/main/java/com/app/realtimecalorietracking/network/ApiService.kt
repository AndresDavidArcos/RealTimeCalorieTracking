package com.app.realtimecalorietracking.network

import com.app.realtimecalorietracking.model.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("parser")
    fun getFoodCalories(
        @Query("ingr") ingredient: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String
    ): Call<FoodResponse>
}
