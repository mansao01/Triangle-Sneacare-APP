package com.mansao.trianglesneacare.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class CustomerAddress(


	@field:SerializedName("notes")
	val notes: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("receiverName")
	val receiverName: String,

	@field:SerializedName("latitude")
	val latitude: Double,

	@field:SerializedName("fullAddress")
	val fullAddress: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("longitude")
	val longitude: Double
)
