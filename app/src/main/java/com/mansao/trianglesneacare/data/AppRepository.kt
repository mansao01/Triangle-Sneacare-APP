package com.mansao.trianglesneacare.data

import com.mansao.trianglesneacare.data.network.ApiService
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.response.GetDriversResponse
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.data.network.response.RegisterDriverResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse
import com.mansao.trianglesneacare.data.preferences.AppPreferences
import com.mansao.trianglesneacare.utils.CameraUtils
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

interface AppRepository {
    //    network
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse
    suspend fun registerDriver(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        file: File
    ): RegisterDriverResponse

    suspend fun login(loginRequest: LoginRequest): LoginResponse
    suspend fun getProfile(token: String): ProfileResponse
    suspend fun getDrivers(): GetDriversResponse
    suspend fun logout(token: String): OnlyMsgResponse

    //    preferences
    suspend fun saveAccessToken(token: String)
    suspend fun saveIsLoginState(isLogin: Boolean)
    suspend fun saveUsername(name: String)
    suspend fun saveRole(role: String)
    suspend fun getAccessToken(): String?
    suspend fun getUsername(): String?
    suspend fun getRole(): String?
    suspend fun getLoginState(): Flow<Boolean>
    suspend fun clearToken()
    suspend fun clearUsername()
    suspend fun clearRoleName()
}

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appPreferences: AppPreferences
) : AppRepository {
    //    network
    override suspend fun register(registerRequest: RegisterRequest): RegisterResponse =
        apiService.register(registerRequest)

    override suspend fun registerDriver(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        file: File
    ): RegisterDriverResponse {
        val compressedFile = CameraUtils.reduceFileImage(file)
        val nameBody = name.toRequestBody("text/plain".toMediaType())
        val emailBody = email.toRequestBody("text/plain".toMediaType())
        val passwordBody = password.toRequestBody("text/plain".toMediaType())
        val addressBody = address.toRequestBody("text/plain".toMediaType())
        val phoneBody = phone.toRequestBody("text/plain".toMediaType())
        val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestImageFile
        )
        return apiService.registerDriver(
            nameBody,
            emailBody,
            passwordBody,
            addressBody,
            phoneBody,
            imageMultipart
        )

    }

    override suspend fun login(loginRequest: LoginRequest): LoginResponse =
        apiService.login(loginRequest)

    override suspend fun getProfile(token: String): ProfileResponse = apiService.getProfile(token)

    override suspend fun getDrivers(): GetDriversResponse = apiService.getDrivers()

    override suspend fun logout(token: String): OnlyMsgResponse = apiService.logout(token)

    //    preferences
    override suspend fun saveAccessToken(token: String) = appPreferences.saveAccessToken(token)

    override suspend fun saveIsLoginState(isLogin: Boolean) =
        appPreferences.saveIsLoginState(isLogin)

    override suspend fun saveUsername(name: String) = appPreferences.saveUsername(name)


    override suspend fun saveRole(role: String) = appPreferences.saveRole(role)

    override suspend fun getAccessToken(): String? = appPreferences.getAccessToken()

    override suspend fun getUsername(): String? = appPreferences.getUsername()

    override suspend fun getRole(): String? = appPreferences.getRole()
    override suspend fun getLoginState(): Flow<Boolean> = appPreferences.getIsLoginState()

    override suspend fun clearToken() = appPreferences.clearTokens()

    override suspend fun clearUsername() = appPreferences.clearUsername()

    override suspend fun clearRoleName() = appPreferences.clearRoleName()
}