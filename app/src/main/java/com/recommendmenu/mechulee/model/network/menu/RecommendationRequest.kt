package com.recommendmenu.mechulee.model.network.menu

import com.google.gson.annotations.SerializedName
import com.recommendmenu.mechulee.model.data.IngredientInfo

data class RecommendationRequest(
    @SerializedName("ingredientList") val ingredientList: ArrayList<IngredientInfo>,
    @SerializedName("nowAddress") val nowAddress: String
)