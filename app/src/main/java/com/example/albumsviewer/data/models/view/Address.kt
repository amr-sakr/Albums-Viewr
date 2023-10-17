package com.example.albumsviewer.data.models.view

import com.example.albumsviewer.data.models.network.AddressAPI

data class Address(
    val street: String? = null,
    val city: String? = null,
)

fun AddressAPI.toAddress() = Address(
    street = street,
    city = city
)