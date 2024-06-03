package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.createTransaction

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.ChargePaymentRequest
import com.mansao.trianglesneacare.data.network.request.CreateTransactionRequest
import com.mansao.trianglesneacare.data.network.response.CalculateDistanceResponse
import com.mansao.trianglesneacare.data.network.response.ChargePaymentResponse
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

    private var _chargePaymentUiState: MutableStateFlow<UiState<ChargePaymentResponse>> =
        MutableStateFlow(UiState.Standby)
    val chargePaymentUiState: Flow<UiState<ChargePaymentResponse>> = _chargePaymentUiState

    private var _addressId = MutableStateFlow("")
    val addressId: Flow<String> = _addressId

    private var _deliveryMethod = MutableStateFlow("")
    val deliveryMethod: Flow<String> = _deliveryMethod

    private var _paymentMethod = MutableStateFlow("")
    val paymentMethod: Flow<String> = _paymentMethod


    private var _totalItems = MutableStateFlow(0)
    val totalItems: Flow<Int> = _totalItems

    private var _totalPrice = MutableStateFlow(0.0)

    private var _distance = MutableStateFlow(0.0)
    val distance: Flow<Double> = _distance

    private var _deliveryFee = MutableStateFlow(0.0)
    val deliveryFee: Flow<Double> = _deliveryFee

    private var _totalShouldPay = MutableStateFlow(0.0)
    val totalShouldPay: Flow<Double> = _totalShouldPay

    private var _cartId = MutableStateFlow("")
    val cartId: Flow<String> = _cartId

    private var _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading



    fun setDistance(distance: Double) {
        _distance.value = distance
    }

    fun setTotalItem(totalItem: Int) {
        _totalItems.value = totalItem
    }

    fun setTotalPrice(totalPrice: Double) {
        _totalPrice.value = totalPrice
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

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
            _addressUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _addressUiState.value = UiState.Error(e.message.toString())
        }
    }

    fun setAddressId(addressId: String) {
        _addressId.value = addressId
    }

    fun setDeliveryMethod(deliveryMethod: String) {
        _deliveryMethod.value = deliveryMethod
    }

    fun setPaymentMethod(paymentMethod: String) {
        _paymentMethod.value = paymentMethod
    }

    fun setCartId(cartId: String) {
        _cartId.value = cartId
    }

    fun calculateDistance(latLngOrigin: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.calculateDistance(latLngOrigin)
            _calculateDistanceUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _calculateDistanceUiState.value = UiState.Error(e.message.toString())
        }
    }



    fun createTransaction(
        cartId: String,
        customerAddressId: String,
        deliveryMethod: String,
        paymentMethod: String,
        totalPurchasePrice: Int
    ) = viewModelScope.launch {
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""

            val result = appRepositoryImpl.createTransaction(
                CreateTransactionRequest(
                    cartId = cartId,
                    deliveryMethod = deliveryMethod,
                    paymentMethod = paymentMethod,
                    customerAddressId = customerAddressId,
                    userId = userId,
                    totalPurchasePrice = totalPurchasePrice
                )
            )
            _createTransactionUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _createTransactionUiState.value = UiState.Error(e.message.toString())

        }
    }

    fun calculateTotalTransaction() {
        val totalItem = _totalPrice.value
        val distance =
            if (_deliveryMethod.value == "Deliver to home") _distance.value * 2 else _distance.value
        val perKm = 2000
        val result = totalItem + (distance * perKm.toDouble())
        Log.d("calculation", "$totalItem + ($distance + $perKm)")

        _deliveryFee.value = perKm * distance
        _totalShouldPay.value = result

    }

    fun chargeTransaction(
        transactionId: String,
    ) = viewModelScope.launch {
        val totalPayment = _totalShouldPay.value
        val username = appRepositoryImpl.getUsername() ?: ""
        val email = appRepositoryImpl.getUserEmail() ?: ""
        try {
            val result = appRepositoryImpl.chargePayment(
                ChargePaymentRequest(
                    transactionId,
                    totalPayment.toInt(),
                    email,
                    username
                )
            )
            _chargePaymentUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _chargePaymentUiState.value = UiState.Error(e.message.toString())

        }
    }



}