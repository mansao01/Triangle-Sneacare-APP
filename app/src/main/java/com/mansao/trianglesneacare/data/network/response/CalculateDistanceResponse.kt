package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class CalculateDistanceResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: CalculateDistanceData
)

data class CalculateDistanceData(

	@field:SerializedName("destination_addresses")
	val destinationAddresses: List<String>,

	@field:SerializedName("origin_addresses")
	val originAddresses: List<String>,

	@field:SerializedName("rows")
	val rows: List<RowsItem>,

	@field:SerializedName("status")
	val status: String
)

data class RowsItem(

	@field:SerializedName("elements")
	val elements: List<ElementsItem>
)


data class ElementsItem(

	@field:SerializedName("duration")
	val duration: Duration,

	@field:SerializedName("distance")
	val distance: Distance,

	@field:SerializedName("status")
	val status: String
)

data class Distance(

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("value")
	val value: Int
)



data class Duration(

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("value")
	val value: Int
)
