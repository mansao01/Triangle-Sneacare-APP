package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetDriversResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("drivers")
	val drivers: List<DriversItem>
)

data class DriversItem(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("role")
	val role: Role? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("pictureUrl")
	val pictureUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
