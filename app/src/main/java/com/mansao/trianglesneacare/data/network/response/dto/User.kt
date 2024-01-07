package com.mansao.trianglesneacare.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("role")
    val role: Role,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String
)
