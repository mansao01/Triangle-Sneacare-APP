package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetServicesByCategoryIdResponse(

	@field:SerializedName("service")
	val service: List<ServiceItem>
)

data class Category(

	@field:SerializedName("itemType")
	val itemType: String,

	@field:SerializedName("id")
	val id: Int,

)

data class ServiceItem(

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("serviceDescription")
	val serviceDescription: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("serviceName")
	val serviceName: Any,

	@field:SerializedName("category")
	val category: Category,

	@field:SerializedName("categoryId")
	val categoryId: Int,

)
