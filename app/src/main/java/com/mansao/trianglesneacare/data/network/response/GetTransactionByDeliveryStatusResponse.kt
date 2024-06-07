package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem

data class GetTransactionByDeliveryStatusResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("transactions")
	val transactions: List<TransactionsItem>,


)

//data class TransactionsItem(
//
//	@field:SerializedName("deliveryMethod")
//	val deliveryMethod: String,
//
//	@field:SerializedName("totalPurchasePrice")
//	val totalPurchasePrice: Int,
//
//	@field:SerializedName("paymentMethod")
//	val paymentMethod: String,
//
//	@field:SerializedName("id")
//	val id: String,
//
//	@field:SerializedName("items")
//	val items: List<ItemsItem>,
//
//	@field:SerializedName("cart")
//	val cart: String,
//
//	@field:SerializedName("deliveryStatus")
//	val deliveryStatus: String,
//
//	@field:SerializedName("paymentStatus")
//	val paymentStatus: String
//)

//data class ItemsItem(
//
//	@field:SerializedName("washStatus")
//	val washStatus: String,
//
//	@field:SerializedName("price")
//	val price: Int,
//
//	@field:SerializedName("imageUrl")
//	val imageUrl: String,
//
//	@field:SerializedName("id")
//	val id: String,
//
//	@field:SerializedName("serviceName")
//	val serviceName: String
//)
