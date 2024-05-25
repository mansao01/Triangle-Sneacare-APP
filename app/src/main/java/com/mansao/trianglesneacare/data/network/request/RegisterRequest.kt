package com.mansao.trianglesneacare.data.network.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val roleId: String = "3bf06a3e-71e3-4888-b720-e520cb2249b3"
)

/* notes : role id
* 3bf06a3e-71e3-4888-b720-e520cb2249b3 -> customer
* 52ba4f20-4554-419b-a22d-6b7c59813c9a -> owner
* 6b7f1778-7a68-421a-a96d-4c46a38b4d31 -> driver
* b65403c0-cbeb-449a-89f6-2989f778645b -> service
* */
