package com.recommendmenu.mechulee.model.network.ingredient

import retrofit2.Call
import retrofit2.http.GET

interface IngredientService {
    @GET("/allingredient")
    fun getAllIngredient(): Call<IngredientDto>
}