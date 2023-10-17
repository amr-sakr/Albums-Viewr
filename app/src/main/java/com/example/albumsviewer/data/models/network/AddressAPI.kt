package com.example.albumsviewer.data.models.network


import com.google.gson.annotations.SerializedName

data class AddressAPI(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("geo")
    val geo: GeoAPI? = null,
    @SerializedName("street")
    val street: String? = null,
    @SerializedName("suite")
    val suite: String? = null,
    @SerializedName("zipcode")
    val zipcode: String? = null
)