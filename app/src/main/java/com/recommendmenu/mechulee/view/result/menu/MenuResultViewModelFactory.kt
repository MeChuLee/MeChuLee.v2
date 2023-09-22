package com.recommendmenu.mechulee.view.result.menu

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.recommendmenu.mechulee.LikeData

class MenuResultViewModelFactory(private val dataStore: DataStore<LikeData>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MenuResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MenuResultViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}