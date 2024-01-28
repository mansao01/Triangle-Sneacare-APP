package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(

	@field:SerializedName("data")
	val data: GeoCodingData,

	@field:SerializedName("message")
	val message: String
)

data class AddressComponentsItem(

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("short_name")
	val shortName: String,

	@field:SerializedName("long_name")
	val longName: String
)

data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String,

	@field:SerializedName("global_code")
	val globalCode: String
)

data class ResultsItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("address_components")
	val addressComponents: List<AddressComponentsItem>,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode,

	@field:SerializedName("place_id")
	val placeId: String
)

data class Viewport(

	@field:SerializedName("southwest")
	val southwest: Southwest,

	@field:SerializedName("northeast")
	val northeast: Northeast
)

data class Geometry(

	@field:SerializedName("viewport")
	val viewport: Viewport,

	@field:SerializedName("location")
	val location: Location,

	@field:SerializedName("location_type")
	val locationType: String
)

data class Southwest(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class Location(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)

data class GeoCodingData(

	@field:SerializedName("results")
	val results: List<ResultsItem>,

	@field:SerializedName("status")
	val status: String
)

data class Northeast(

	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("lat")
	val lat: Double
)
