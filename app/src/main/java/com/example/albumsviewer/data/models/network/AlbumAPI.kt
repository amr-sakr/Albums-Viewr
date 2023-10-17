package com.example.albumsviewer.data.models.network


import com.google.gson.annotations.SerializedName

data class AlbumAPI(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("userId")
    val userId: Int? = null
)