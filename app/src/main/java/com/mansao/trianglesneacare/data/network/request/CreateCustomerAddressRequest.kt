package com.mansao.trianglesneacare.data.network.request

data class CreateCustomerAddressRequest(
    val title: String,
    val receiverName: String,
    val phone: String,
    val fullAddress: String,
    val note: String
)
