package com.recommendmenu.mechulee.model.data

// Model - RecyclerView에 들어가는 데이터 모델
// image - text - rating - classification 순서
data class IngredientInfo(val imageResId: Int, val title: String, var rating: Float, val classification: String)
