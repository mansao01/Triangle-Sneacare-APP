package com.mansao.trianglesneacare.ui.screen.section.driver.deliver.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliverDetailViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _updateDeliveryStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updateDeliveryStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updateDeliveryStatusUiState

    private var _updatePaymentStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updatePaymentStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updatePaymentStatusUiState

    fun updateDeliveryStatusById(transactionId: String) = viewModelScope.launch {
        _updateDeliveryStatusUiState.value = UiState.Loading
        try {
            val result =
                appRepositoryImpl.updateDeliveryStatusById(transactionId, "already delivered to customer")
            _updateDeliveryStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updateDeliveryStatusUiState.value = UiState.Error(e.message.toString())
        }
    }

    fun updatePaymentStatus(transactionId: String) = viewModelScope.launch {
        _updatePaymentStatusUiState.value = UiState.Loading
        try {
            val result = appRepositoryImpl.updatePaymentStatus(transactionId, "settlement")
            _updatePaymentStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updatePaymentStatusUiState.value = UiState.Error(e.message.toString())

        }
    }
}