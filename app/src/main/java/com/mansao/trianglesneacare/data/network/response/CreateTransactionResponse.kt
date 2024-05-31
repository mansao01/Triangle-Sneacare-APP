package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class CreateTransactionResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("transaction")
	val transaction: Transaction
)

data class Cart(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("totalPrice")
	val totalPrice: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class Transaction(

	@field:SerializedName("deliveryMethod")
	val deliveryMethod: String,

	@field:SerializedName("paymentMethod")
	val paymentMethod: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("cart")
	val cart: Cart
)
