package com.example.albumsviewer.data.models.view

import com.example.albumsviewer.data.models.network.PhotoAPI

data class Photo(
    val id: Int = -1,
    val thumbnailUrl: String? = null,
    val title: String? = null,
    val url: String? = null
)

fun PhotoAPI.toPhoto() =
    Photo(
        id = id ?: -1,
        thumbnailUrl = thumbnailUrl,
        title = title,
        url = url
    )