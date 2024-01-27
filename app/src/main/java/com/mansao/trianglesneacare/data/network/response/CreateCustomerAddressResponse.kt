package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class CreateCustomerAddressResponse(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("message")
	val message: String
)

data class Address(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("receiverName")
	val receiverName: String? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("fullAddress")
	val fullAddress: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
