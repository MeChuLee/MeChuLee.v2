package com.recommendmenu.mechulee.model.network.search

import com.google.gson.annotations.SerializedName

data class SearchDto(

    @SerializedName("lastBuildDate")
    val lastBuildDate: String,

    @SerializedName ("total")
    var total :Int,

    @SerializedName ("items")
    var items :List<Item>
)

data class Item(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String?,
    @SerializedName("mapx") val mapx: String,
    @SerializedName("mapy") val mapy: String
)
