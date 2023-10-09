package com.recommendmenu.mechulee.model.data

import java.io.Serializable

data class MenuInfo(val name: String, val ingredients: ArrayList<String>, val category: String) : Serializable