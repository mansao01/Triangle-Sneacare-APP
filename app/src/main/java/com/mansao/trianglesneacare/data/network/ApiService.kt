package com.mansao.trianglesneacare.data.network

import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.response.GetCustomerAddressesResponse
import com.mansao.trianglesneacare.data.network.response.GetDriversResponse
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.data.network.response.RegisterDriverResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @GET(ApiConst.CHECK_SERVER)
    suspend fun checkServerWork(): OnlyMsgResponse

    @POST(ApiConst.REGISTER)
    suspend fun register(
        @Body registerRequestBody: RegisterRequest
    ): RegisterResponse

    @Multipart
    @POST(ApiConst.REGISTER_DRIVER)
    suspend fun registerDriver(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("address") address: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part
    ): RegisterDriverResponse

    @POST(ApiConst.LOGIN)
    suspend fun login(
        @Body loginRequestBody: LoginRequest
    ): LoginResponse

    @GET(ApiConst.PROFILE)
    suspend fun getProfile(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): ProfileResponse

    @GET(ApiConst.DRIVERS)
    suspend fun getDrivers(): GetDriversResponse

    @GET(ApiConst.GET_CUSTOMER_ADDRESS)
    suspend fun getCustomerAddresses(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): GetCustomerAddressesResponse

    @POST(ApiConst.LOGOUT)
    suspend fun logout(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): OnlyMsgResponse
}