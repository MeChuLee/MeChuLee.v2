package com.recommendmenu.mechulee.model.data

// id - image - text - rating - classification 순서
data class IngredientInfo(
    val imageResId: Int,
    val title: String,
    var rating: Float,
    var classification: String
)