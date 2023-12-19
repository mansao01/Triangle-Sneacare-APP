package com.mansao.trianglesneacare.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("role")
    val role: Role? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null
)
