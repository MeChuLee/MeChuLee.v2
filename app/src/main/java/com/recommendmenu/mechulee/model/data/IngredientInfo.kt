package com.recommendmenu.mechulee.model.data

// Model - RecyclerView에 들어가는 데이터 모델

// id - image - text - rating - classification 순서
data class IngredientInfo(val ingredientId: Int, val imageResId: Int, val title: String, var rating: Float, var classification: String)