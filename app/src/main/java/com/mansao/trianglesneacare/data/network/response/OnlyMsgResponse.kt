package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName

data class OnlyMsgResponse(

	@field:SerializedName("msg")
	val msg: String
)
