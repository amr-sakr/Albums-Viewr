package com.example.albumsviewer.data.models.network


import com.google.gson.annotations.SerializedName

data class GeoAPI(
    @SerializedName("lat")
    val lat: String? = null,
    @SerializedName("lng")
    val lng: String? = null
)