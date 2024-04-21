package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class AddServiceResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("product")
	val product: Product
)

data class ItemType(

	@field:SerializedName("itemType")
	val itemType: String,

	@field:SerializedName("id")
	val id: Int
)

data class Product(

	@field:SerializedName("itemType")
	val itemType: ItemType,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("serviceDescription")
	val serviceDescription: String,

	@field:SerializedName("id")
	val id: Int
)
