package com.mansao.trianglesneacare.data.network.request

data class AddServiceRequest(
    val serviceName: String,
    val price: String,
    val categoryId: Int,
    val serviceDescription: String,
)