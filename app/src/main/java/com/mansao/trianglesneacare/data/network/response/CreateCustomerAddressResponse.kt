package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName
import com.mansao.trianglesneacare.data.network.response.dto.Address

data class CreateCustomerAddressResponse(

	@field:SerializedName("address")
	val address: Address,

	@field:SerializedName("message")
	val message: String
)

