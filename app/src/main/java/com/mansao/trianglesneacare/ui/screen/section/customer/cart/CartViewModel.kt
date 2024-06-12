package com.mansao.trianglesneacare.ui.screen.section.customer.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetCartResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _uiState: MutableStateFlow<UiState<GetCartResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetCartResponse>> = _uiState

    private var _isDeleteSuccess: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isDeleteSuccess: Flow<Boolean> = _isDeleteSuccess

    private var _cartNotFound: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val cartNotFound: Flow<Boolean> = _cartNotFound
    private fun setLoadingState() {
        _uiState.value = UiState.Loading
    }

    fun getCart() = viewModelScope.launch {
        setLoadingState()
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""
            val result = appRepositoryImpl.getCart(userId)
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {

            val errorMessage = when (e) {
                is IOException -> """
                        {"msg": "Service unavailable"}
                    """.trimIndent()

                is HttpException -> {
                    when (e.code()) {
                        400 -> e.response()?.errorBody()?.string().toString()
                        404 -> {
                            _cartNotFound.value = true
                            e.response()?.errorBody()?.string().toString()
                        }
                        // Add more cases for specific HTTP error codes if needed
                        else -> """
                                {"msg": "Error code ${e.message()}"}
                            """.trimIndent()
                    }
                }

                else -> """
                        {"msg": "Service unavailable"}
                    """.trimIndent()
            }
            val gson = Gson()
            val jsonObject = gson.fromJson(errorMessage, Map::class.java) as Map<*, *>
            val msg = jsonObject["msg"]
            _uiState.value = UiState.Error(msg.toString())
        }
    }

    fun deleteOrder(orderId: String) = viewModelScope.launch {
        try {
            appRepositoryImpl.deleteOrder(orderId)
            _isDeleteSuccess.value = true
        } catch (e: Exception) {
            _isDeleteSuccess.value = false
        }
    }

    fun resetDeleteSuccess() {
        _isDeleteSuccess.value = false
    }
}