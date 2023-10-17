package com.example.albumsviewer.data.models.network


import com.google.gson.annotations.SerializedName

data class UserAPI(
    @SerializedName("address")
    val address: AddressAPI? = null,
    @SerializedName("company")
    val company: CompanyAPI? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("website")
    val website: String? = null
)