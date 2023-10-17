package com.example.albumsviewer.data.models.network


import com.google.gson.annotations.SerializedName

data class CompanyAPI(
    @SerializedName("bs")
    val bs: String? = null,
    @SerializedName("catchPhrase")
    val catchPhrase: String? = null,
    @SerializedName("name")
    val name: String? = null
)