package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class AddOrderResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("order")
	val order: Order
)

data class Order(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("washStatus")
	val washStatus: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("serviceId")
	val serviceId: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
