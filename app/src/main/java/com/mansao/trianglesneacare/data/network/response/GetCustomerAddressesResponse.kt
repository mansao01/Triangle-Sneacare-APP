package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetCustomerAddressesResponse(

	@field:SerializedName("address")
	val address: List<AddressItem>,

	@field:SerializedName("message")
	val message: String
)

data class AddressItem(

	@field:SerializedName("notes")
	val notes: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("fullAddress")
	val fullAddress: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("receiverName")
	val receiverName: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double,

)
