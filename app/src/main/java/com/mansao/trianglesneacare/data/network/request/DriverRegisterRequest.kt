package com.mansao.trianglesneacare.data.network.request

data class DriverRegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
)

/* notes : role id
* 1 -> customer
* 2 -> admin
* 3 -> driver
* */
