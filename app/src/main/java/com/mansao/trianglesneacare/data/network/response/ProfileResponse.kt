package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("profile")
	val profile: Profile
)

data class Role(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class Profile(

	@field:SerializedName("role")
	val role: Role,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)
