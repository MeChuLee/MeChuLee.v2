package com.recommendmenu.mechulee.model.data

// id - image - text - rating - classification 순서
data class IngredientInfo(
    val imageResId: Int,
    var title: String,
    var rating: Float,
    var classification: String
)