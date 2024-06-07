package com.mansao.trianglesneacare.data.network.response.dto

import com.google.gson.annotations.SerializedName

data class TransactionsItem(

    @field:SerializedName("deliveryMethod")
    val deliveryMethod: String,

    @field:SerializedName("deliveryStatus")
    val deliveryStatus: String,

    @field:SerializedName("user")
    val user: String,

    @field:SerializedName("totalPurchasePrice")
    val totalPurchasePrice: Int,

    @field:SerializedName("customerAddressId")
    val customerAddressId: String,

    @field:SerializedName("paymentMethod")
    var paymentMethod: String,

    @field:SerializedName("paymentStatus")
    val paymentStatus: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("items")
    val items: List<ItemsItem>,

    @field:SerializedName("cart")
    val cart: String,

    @field:SerializedName("customerAddress")
    val customerAddress: CustomerAddress,
)

data class ItemsItem(

    @field:SerializedName("washStatus")
    val washStatus: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("serviceName")
    val serviceName: String
)
