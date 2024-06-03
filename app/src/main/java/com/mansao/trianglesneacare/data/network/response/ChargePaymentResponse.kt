package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class ChargePaymentResponse(

	@field:SerializedName("redirect_url")
	val redirectUrl: String,

	@field:SerializedName("token")
	val token: String
)
