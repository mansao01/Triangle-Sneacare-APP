package com.mansao.trianglesneacare.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class Role(

    @field:SerializedName("role")
    val role: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)
