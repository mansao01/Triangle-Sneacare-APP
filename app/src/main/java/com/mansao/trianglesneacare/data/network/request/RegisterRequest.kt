package com.mansao.trianglesneacare.data.network.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val roleId: Int = 1
)

/* notes : role id
* 1 -> customer
* 3 -> admin
* 4 -> driver
* */
