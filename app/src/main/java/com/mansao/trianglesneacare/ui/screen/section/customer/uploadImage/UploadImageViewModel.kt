package com.mansao.trianglesneacare.ui.screen.section.customer.uploadImage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.AddToCartRequest
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _uiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<OnlyMsgResponse>> = _uiState
    private fun setLoadingState() {
        _uiState.value = UiState.Loading
    }
    fun setStandbyState() {
        _uiState.value = UiState.Standby

    }

    fun createOrder(
        serviceId: String,
        file: File
    ) = viewModelScope.launch {
        setLoadingState()
        try {
            val userId = appRepositoryImpl.getUserId() ?: ""
            val createOrder = appRepositoryImpl.createOrder("Pending", userId, serviceId, file)
            addToCart(createOrder.order.id, createOrder.order.userId)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }

    private fun addToCart(orderId: String, userId: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.addToCart(AddToCartRequest(orderId, userId))
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }
}