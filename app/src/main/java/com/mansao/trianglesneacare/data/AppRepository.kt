package com.mansao.trianglesneacare.data

import com.mansao.trianglesneacare.data.network.ApiService
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse
import javax.inject.Inject

interface AppRepository {
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse
    suspend fun login(loginRequest: LoginRequest): LoginResponse
}

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AppRepository {
    override suspend fun register(registerRequest: RegisterRequest): RegisterResponse =
        apiService.register(registerRequest)

    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)
}