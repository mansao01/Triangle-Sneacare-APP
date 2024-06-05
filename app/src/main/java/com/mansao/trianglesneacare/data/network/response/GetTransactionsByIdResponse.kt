package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetTransactionsByIdResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("transactions")
	val transactions: List<TransactionsItem>
)

data class ItemsItem(


	@field:SerializedName("washStatus")
	val washStatus: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("cartId")
	val cartId: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("serviceId")
	val serviceId: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)

data class TransactionsItem(

	@field:SerializedName("deliveryMethod")
	val deliveryMethod: String,

	@field:SerializedName("totalPurchasePrice")
	val totalPurchasePrice: Int,

	@field:SerializedName("paymentMethod")
	var paymentMethod: String,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("items")
	val items: List<ItemsItem>,

	@field:SerializedName("cart")
	val cart: String
)
