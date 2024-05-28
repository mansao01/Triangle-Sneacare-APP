package com.mansao.trianglesneacare.data

import com.mansao.trianglesneacare.data.network.ApiService
import com.mansao.trianglesneacare.data.network.request.AddCategoryRequest
import com.mansao.trianglesneacare.data.network.request.AddServiceRequest
import com.mansao.trianglesneacare.data.network.request.CreateCustomerAddressRequest
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.data.network.response.AutoCompleteAddressResponse
import com.mansao.trianglesneacare.data.network.response.CreateCustomerAddressResponse
import com.mansao.trianglesneacare.data.network.response.CustomerDetailAddressResponse
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.data.network.response.GetCategoriesResponse
import com.mansao.trianglesneacare.data.network.response.GetCustomerAddressesResponse
import com.mansao.trianglesneacare.data.network.response.GetProfileDetailResponse
import com.mansao.trianglesneacare.data.network.response.GetServicesByCategoryIdResponse
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.OnlyAccessTokenResponse
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

    suspend fun checkServerIsRunning(): OnlyMsgResponse
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
    suspend fun refreshToken(refreshToken: String): OnlyAccessTokenResponse
    suspend fun getProfile(token: String): ProfileResponse
    suspend fun sendResetPassword(email: String): OnlyMsgResponse
    suspend fun verifyOTP(email: String, otp: String): OnlyMsgResponse
    suspend fun resetPassword(
        email: String,
        password: String,
        confirmPassword: String
    ): OnlyMsgResponse

    suspend fun getProfileDetail(token: String): GetProfileDetailResponse
    suspend fun logout(token: String): OnlyMsgResponse
    suspend fun getCustomerAddresses(token: String): GetCustomerAddressesResponse
    suspend fun createCustomerAddress(
        token: String,
        createCustomerAddressRequest: CreateCustomerAddressRequest
    ): CreateCustomerAddressResponse

    suspend fun deleteCustomerAddress(token: String, id: String): OnlyMsgResponse
    suspend fun getDetailCustomerAddresses(
        token: String,
        addressId: String
    ): CustomerDetailAddressResponse

    suspend fun updateCustomerAddress(
        token: String,
        id: String,
        receiverName: String,
        fullAddress: String,
        note: String,
        title: String,
        phone: String
    ): OnlyMsgResponse

    suspend fun autoCompleteAddress(address: String): AutoCompleteAddressResponse

    suspend fun geocodeWithAddress(address: String): GeocodingResponse
    suspend fun geocodeWithPlaceId(placeId: String): GeocodingResponse
    suspend fun getCategories(): GetCategoriesResponse
    suspend fun getServicesByCategory(categoryId: String): GetServicesByCategoryIdResponse
    suspend fun addCategory(addCategoryRequest: AddCategoryRequest): OnlyMsgResponse
    suspend fun addService(addServiceRequest: AddServiceRequest): OnlyMsgResponse
    suspend fun deleteService(serviceId:String):OnlyMsgResponse
    suspend fun updateService(updateServiceRequest: UpdateServiceRequest):OnlyMsgResponse

    //    preferences
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveIsLoginState(isLogin: Boolean)
    suspend fun saveUsername(name: String)
    suspend fun saveUserId(id: String)
    suspend fun saveRole(role: String)
    suspend fun saveShowBalloonState(showBalloonState: Boolean)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getUsername(): String?
    suspend fun getUserId(): String?
    suspend fun getRole(): String?
    suspend fun getLoginState(): Flow<Boolean>
    suspend fun getBalloonState(): Flow<Boolean>
    suspend fun clearToken()
    suspend fun clearUsername()
    suspend fun clearUserId()
    suspend fun clearRoleName()
}

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appPreferences: AppPreferences
) : AppRepository {
    //    network

    override suspend fun checkServerIsRunning(): OnlyMsgResponse = apiService.checkServerWork()

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

    override suspend fun refreshToken(refreshToken: String): OnlyAccessTokenResponse =
        apiService.refreshToken(refreshToken)

    override suspend fun sendResetPassword(email: String): OnlyMsgResponse =
        apiService.sendResetPassword(email)

    override suspend fun verifyOTP(email: String, otp: String) = apiService.verifyOtp(email, otp)

    override suspend fun resetPassword(email: String, password: String, confirmPassword: String) =
        apiService.resetPassword(email, password, confirmPassword)

    override suspend fun getProfile(token: String): ProfileResponse = apiService.getProfile(token)
    override suspend fun getProfileDetail(token: String): GetProfileDetailResponse =
        apiService.getProfileDetail(token)


    override suspend fun logout(token: String): OnlyMsgResponse = apiService.logout(token)

    override suspend fun getCustomerAddresses(token: String): GetCustomerAddressesResponse =
        apiService.getCustomerAddresses(token)

    override suspend fun getDetailCustomerAddresses(
        token: String,
        addressId: String
    ): CustomerDetailAddressResponse = apiService.getDetailCustomerAddresses(token, addressId)

    override suspend fun createCustomerAddress(
        token: String,
        createCustomerAddressRequest: CreateCustomerAddressRequest
    ): CreateCustomerAddressResponse =
        apiService.createCustomerAddress(token, createCustomerAddressRequest)

    override suspend fun deleteCustomerAddress(token: String, id: String): OnlyMsgResponse =
        apiService.deleteCustomerAddress(token, id)

    override suspend fun updateCustomerAddress(
        token: String,
        id: String,
        receiverName: String,
        fullAddress: String,
        note: String,
        title: String,
        phone: String
    ): OnlyMsgResponse =
        apiService.updateCustomerAddress(token, id, receiverName, fullAddress, note, title, phone)

    override suspend fun autoCompleteAddress(address: String): AutoCompleteAddressResponse =
        apiService.autoCompleteAddress(address)

    override suspend fun geocodeWithAddress(address: String): GeocodingResponse =
        apiService.geocodeWithAddress(address)

    override suspend fun geocodeWithPlaceId(placeId: String): GeocodingResponse =
        apiService.geocodeWithPlaceId(placeId)

    override suspend fun getCategories(): GetCategoriesResponse = apiService.getCategories()
    override suspend fun getServicesByCategory(categoryId: String): GetServicesByCategoryIdResponse {
        return apiService.getServicesByCategoryId(categoryId)
    }

    override suspend fun addCategory(addCategoryRequest: AddCategoryRequest): OnlyMsgResponse {
        return apiService.addCategory(addCategoryRequest)
    }

    override suspend fun addService(addServiceRequest: AddServiceRequest): OnlyMsgResponse {
        return apiService.addService(addServiceRequest)
    }

    override suspend fun deleteService(serviceId: String): OnlyMsgResponse {
        return apiService.deleteService(serviceId)
    }

    override suspend fun updateService(updateServiceRequest: UpdateServiceRequest): OnlyMsgResponse {
        return apiService.updateService(updateServiceRequest)
    }

    //    preferences
    override suspend fun saveAccessToken(token: String) = appPreferences.saveAccessToken(token)
    override suspend fun saveRefreshToken(token: String) = appPreferences.saveRefreshToken(token)

    override suspend fun saveIsLoginState(isLogin: Boolean) =
        appPreferences.saveIsLoginState(isLogin)

    override suspend fun saveUsername(name: String) = appPreferences.saveUsername(name)

    override suspend fun saveUserId(id: String) = appPreferences.saveUserId(id)

    override suspend fun getUserId(): String? = appPreferences.getUserId()

    override suspend fun clearUserId() = appPreferences.clearUserId()
    override suspend fun saveRole(role: String) = appPreferences.saveRole(role)
    override suspend fun saveShowBalloonState(showBalloonState: Boolean) =
        appPreferences.saveShowBalloonState(showBalloonState)

    override suspend fun getAccessToken(): String? = appPreferences.getAccessToken()
    override suspend fun getRefreshToken(): String? = appPreferences.getRefreshToken()

    override suspend fun getUsername(): String? = appPreferences.getUsername()

    override suspend fun getRole(): String? = appPreferences.getRole()
    override suspend fun getLoginState(): Flow<Boolean> = appPreferences.getIsLoginState()

    override suspend fun getBalloonState(): Flow<Boolean> = appPreferences.getShowBalloonState()


    override suspend fun clearToken() = appPreferences.clearTokens()

    override suspend fun clearUsername() = appPreferences.clearUsername()

    override suspend fun clearRoleName() = appPreferences.clearRoleName()
}