package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName
import com.mansao.trianglesneacare.data.network.response.dto.User

data class RegisterDriverResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("preview")
	val preview: Boolean,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("info")
	val info: String
)



