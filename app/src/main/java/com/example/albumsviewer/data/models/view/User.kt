package com.example.albumsviewer.data.models.view

import com.example.albumsviewer.data.models.network.UserAPI

data class User(
    val id: Int = -1,
    val name: String? = null,
    val address: Address? = null,
)

fun UserAPI.toUser() = User(
    id = id ?: -1,
    name = name,
    address = address?.toAddress()
)