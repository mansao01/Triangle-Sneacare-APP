package com.mansao.trianglesneacare.data.network.request

data class AddServiceRequest(
    val serviceName: String,
    val price: String,
    val categoryId: String,
    val serviceDescription: String,
)