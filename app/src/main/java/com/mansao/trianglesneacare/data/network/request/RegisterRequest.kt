package com.mansao.trianglesneacare.data.network.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val roleId: Int = 1
)

/* notes : role id
* 1 -> customer
* 2 -> admin
* 3 -> driver
* */
