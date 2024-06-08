package com.mansao.trianglesneacare.data.network.response

import com.google.gson.annotations.SerializedName
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem

data class GetTransactionByMonthResponse(

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("transactions")
    val transactions: List<TransactionsItem>,

    )
