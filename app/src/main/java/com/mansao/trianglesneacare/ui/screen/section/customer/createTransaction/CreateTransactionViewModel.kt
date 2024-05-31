package com.mansao.trianglesneacare.ui.screen.section.customer.createTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.CreateTransactionRequest
import com.mansao.trianglesneacare.data.network.response.CalculateDistanceResponse
import com.mansao.trianglesneacare.data.network.response.CreateTransactionResponse
import com.mansao.trianglesneacare.data.network.response.GetCartResponse
import com.mansao.trianglesneacare.data.network.response.GetCustomerAddressesResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTransactionViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _cartUiState: MutableStateFlow<UiState<GetCartResponse>> =
        MutableStateFlow(UiState.Standby)
    val cartUiState: Flow<UiState<GetCartResponse>> = _cartUiState

    private var _addressUiState: MutableStateFlow<UiState<GetCustomerAddressesResponse>> =
        MutableStateFlow(UiState.Standby)
    val addressUiState: Flow<UiState<GetCustomerAddressesResponse>> = _addressUiState

    private var _calculateDistanceUiState: MutableStateFlow<UiState<CalculateDistanceResponse>> =
        MutableStateFlow(UiState.Standby)
    val calculateDistanceUiState: Flow<UiState<CalculateDistanceResponse>> =
        _calculateDistanceUiState

    private var _createTransactionUiState: MutableStateFlow<UiState<CreateTransactionResponse>> =
        MutableStateFlow(UiState.Standby)
    val createTransactionUiState: Flow<UiState<CreateTransactionResponse>> =
        _createTransactionUiState



    fun getCart() = viewModelScope.launch {
        _cartUiState.value = UiState.Loading
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""
            val result = appRepositoryImpl.getCart(userId)
            _cartUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _cartUiState.value = UiState.Error(e.message.toString())

        }
    }


    fun getAddresses() = viewModelScope.launch {
        try {
            val token = appRepositoryImpl.getAccessToken()
            val result = appRepositoryImpl.getCustomerAddresses("Bearer $token")

        } catch (e: Exception) {
        }
    }

    fun calculateDistance(latLngOrigin: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.calculateDistance(latLngOrigin)
        } catch (e: Exception) {
        }
    }

    fun createTransaction(
        cartId: String,
        deliveryMethod: String,
        paymentMethod: String,
        customerAddressId: String
    ) = viewModelScope.launch {
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""

            val result = appRepositoryImpl.createTransaction(
                CreateTransactionRequest(
                    cartId,
                    deliveryMethod,
                    paymentMethod,
                    customerAddressId,
                    userId
                )
            )
            _createTransactionUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _createTransactionUiState.value = UiState.Error(e.message.toString())

        }
    }

}