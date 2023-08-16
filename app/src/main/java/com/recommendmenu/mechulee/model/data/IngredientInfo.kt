package com.recommendmenu.mechulee.model.data

// Model - RecyclerView에 들어가는 데이터 모델
// image - text - rating 순서
data class IngredientInfo(val imageResId: Int, val title: String, var rating: Float)
// -> val classification: String으로 분류 속성 추가해서 코드 수정하기,