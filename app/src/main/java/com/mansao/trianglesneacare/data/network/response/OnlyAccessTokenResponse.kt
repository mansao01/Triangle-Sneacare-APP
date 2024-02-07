package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class OnlyAccessTokenResponse(

	@field:SerializedName("accessToken")
	val accessToken: String
)
