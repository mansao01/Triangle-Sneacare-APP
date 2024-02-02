package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetProfileDetailResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
