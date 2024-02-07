package com.mansao.trianglesneacare.data.network

import com.mansao.trianglesneacare.data.network.request.CreateCustomerAddressRequest
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.data.network.response.AutoCompleteAddressResponse
import com.mansao.trianglesneacare.data.network.response.CreateCustomerAddressResponse
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.data.network.response.GetCustomerAddressesResponse
import com.mansao.trianglesneacare.data.network.response.GetDriversResponse
import com.mansao.trianglesneacare.data.network.response.GetProfileDetailResponse
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.OnlyAccessTokenResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.data.network.response.RegisterDriverResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

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

    @POST(ApiConst.REFRESH_ACCESS_TOKEN)
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken:String
    ):OnlyAccessTokenResponse

    @GET(ApiConst.PROFILE)
    suspend fun getProfile(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): ProfileResponse

    @GET(ApiConst.PROFILE_DETAIL)
    suspend fun getProfileDetail(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): GetProfileDetailResponse


    @GET(ApiConst.DRIVERS)
    suspend fun getDrivers(): GetDriversResponse

    @GET(ApiConst.GET_CUSTOMER_ADDRESS)
    suspend fun getCustomerAddresses(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): GetCustomerAddressesResponse

    @POST(ApiConst.CREATE_CUSTOMER_ADDRESS)
    suspend fun createCustomerAddress(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String,
        @Body createCustomerAddressRequest: CreateCustomerAddressRequest
    ): CreateCustomerAddressResponse

    @DELETE(ApiConst.DELETE_CUSTOMER_ADDRESS)
    suspend fun deleteCustomerAddress(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String,
        @Query("id") id: Int
    ):OnlyMsgResponse

    @PUT(ApiConst.UPDATE_CUSTOMER_ADDRESS)
    suspend fun updateCustomerAddress(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String,
        @Query("id") id: Int,
        @Query("receiverName") receiverName: String,
        @Query("fullAddress") fullAddress: String,
        @Query("note") note: String,
    ):OnlyMsgResponse

    @GET(ApiConst.AUTO_COMPLETE_ADDRESS)
    suspend fun autoCompleteAddress(
        @Query("address") address: String
    ): AutoCompleteAddressResponse


    @GET(ApiConst.GEOCODING_ADDRESS)
    suspend fun geocodeWithAddress(
        @Query("address") address: String
    ): GeocodingResponse

    @GET(ApiConst.GEOCODING_PLACE_ID)
    suspend fun geocodeWithPlaceId(
        @Query("placeId") placeId: String
    ): GeocodingResponse

    @POST(ApiConst.LOGOUT)
    suspend fun logout(
        @Header(ApiConst.AUTHORIZATION_KEY) token: String
    ): OnlyMsgResponse
}