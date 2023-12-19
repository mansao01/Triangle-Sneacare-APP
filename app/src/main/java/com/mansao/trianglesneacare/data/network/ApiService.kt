package com.mansao.trianglesneacare.data.network

import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST(ApiConst.REGISTER)
    suspend fun register(
        @Body registerRequestBody: RegisterRequest
    ): RegisterResponse

    @POST(ApiConst.LOGIN)
    suspend fun login(
        @Body loginRequestBody:LoginRequest
    ):LoginResponse
}