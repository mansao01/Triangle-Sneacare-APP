package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GetCategoriesResponse(

	@field:SerializedName("categories")
	val categories: List<CategoriesItem>
)

data class CategoriesItem(


	@field:SerializedName("itemType")
	val itemType: String,

	@field:SerializedName("id")
	val id: String,


)
