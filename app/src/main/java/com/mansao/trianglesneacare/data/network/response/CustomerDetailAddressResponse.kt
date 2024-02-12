package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class CustomerDetailAddressResponse(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("message")
	val message: String
)

data class Address(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("notes")
	val notes: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("receiverName")
	val receiverName: String,

	@field:SerializedName("latitude")
	val latitude: Any,

	@field:SerializedName("fullAddress")
	val fullAddress: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("longitude")
	val longitude: Any,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
