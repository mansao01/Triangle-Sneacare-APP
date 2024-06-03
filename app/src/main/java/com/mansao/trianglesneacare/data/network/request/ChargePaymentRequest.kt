package com.mansao.trianglesneacare.data.network.request

data class ChargePaymentRequest(
    val orderId:String,
    val totalPrice:Int,
    val email:String,
    val name:String
)
