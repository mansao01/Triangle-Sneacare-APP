package com.mansao.trianglesneacare.data.network.request

data class CreateTransactionRequest(
    val cartId: String,
    val deliveryMethod: String,
    val paymentMethod: String,
    val totalPurchasePrice: Int,
    val customerAddressId: String,
    val userId: String
)
