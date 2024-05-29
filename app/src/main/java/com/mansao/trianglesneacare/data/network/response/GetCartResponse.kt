package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetCartResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("totalPrice")
    val totalPrice: Int,

    @field:SerializedName("cartId")
    val cartId: String,

    @field:SerializedName("items")
    val items: List<CartItems>
)

data class Service(

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("serviceDescription")
    val serviceDescription: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("serviceName")
    val serviceName: String,

    @field:SerializedName("categoryId")
    val categoryId: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

data class CartItems(

    @field:SerializedName("washStatus")
    val washStatus: String,

    @field:SerializedName("service")
    val service: Service,

    @field:SerializedName("imageUrl")
    val imageUrl: String,

    @field:SerializedName("id")
    val id: String
)
