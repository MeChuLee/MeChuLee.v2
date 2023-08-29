package com.recommendmenu.mechulee.view.recommend_menu.ai.ingredient_rate

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.recommendmenu.mechulee.RatingData

class IngredientRateViewModelFactory(private val dataStore: DataStore<RatingData>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IngredientRateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientRateViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}