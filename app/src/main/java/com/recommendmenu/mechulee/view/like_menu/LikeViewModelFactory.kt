package com.recommendmenu.mechulee.view.like_menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.recommendmenu.mechulee.LikeData

class LikeViewModelFactory(private val dataStore: DataStore<LikeData>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LikeViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}