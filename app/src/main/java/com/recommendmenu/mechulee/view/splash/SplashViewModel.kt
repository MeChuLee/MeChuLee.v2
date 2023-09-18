package com.recommendmenu.mechulee.view.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.recommendmenu.mechulee.utils.NetworkUtils

class SplashViewModel: ViewModel() {

    val allComplete = MutableLiveData<Boolean>()

    private var completeGetIngredient = false
    private var completeGetMenu = false

    init {
        requestAllIngredient()
        requestAllMenu()
    }

    private fun requestAllIngredient() {
        NetworkUtils.requestAllIngredient(onResult = {
            completeGetIngredient = true

            if (completeGetMenu) allComplete.value = true
        })
    }

    private fun requestAllMenu() {
        NetworkUtils.requestAllMenu(onResult = {
            completeGetMenu = true

            if (completeGetIngredient) allComplete.value = true
        })
    }
}