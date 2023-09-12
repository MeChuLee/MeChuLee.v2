package com.recommendmenu.mechulee.model.network.ingredient

import com.google.gson.annotations.SerializedName
import com.recommendmenu.mechulee.model.data.IngredientInfo

data class IngredientDto (
    @SerializedName("ingredientList")
    val ingredientList: List<IngredientInfo>
)