package com.example.albumsviewer.data.models.view

import com.example.albumsviewer.data.models.network.AlbumAPI


data class Album(
    val id: Int = -1,
    val title: String? = null,
)


fun AlbumAPI.toAlbum() = Album(
    id = id ?: -1,
    title = title
)