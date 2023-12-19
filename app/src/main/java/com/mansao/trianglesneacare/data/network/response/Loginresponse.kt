package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName
import com.mansao.trianglesneacare.data.network.response.dto.User

data class LoginResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)

