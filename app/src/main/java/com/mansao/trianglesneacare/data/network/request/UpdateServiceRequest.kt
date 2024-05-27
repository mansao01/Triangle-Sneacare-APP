package com.mansao.trianglesneacare.data.network.request

data class UpdateServiceRequest(
    val id: String,
    val serviceName: String,
    val price: Int,
    val serviceDescription: String
)