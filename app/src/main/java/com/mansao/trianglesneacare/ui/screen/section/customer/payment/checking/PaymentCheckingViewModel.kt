package com.mansao.trianglesneacare.ui.screen.section.customer.payment.checking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetPaymentStatusResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentCheckingViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _getOnlinePaymentStatusUiState: MutableStateFlow<UiState<GetPaymentStatusResponse>> =
        MutableStateFlow(UiState.Standby)
    val getOnlinePaymentStatusUiState: Flow<UiState<GetPaymentStatusResponse>> =
        _getOnlinePaymentStatusUiState


    private var _updatePaymentStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updatePaymentStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updatePaymentStatusUiState

    private var _updateDeliveryStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updateDeliveryStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updateDeliveryStatusUiState

    fun setToStandby() {
        _getOnlinePaymentStatusUiState.value = UiState.Standby
    }

    fun getPaymentStatus(transactionId: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.getPaymentStatus(transactionId)
            _getOnlinePaymentStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _getOnlinePaymentStatusUiState.value = UiState.Error(e.message.toString())

        }
    }

    fun updatePaymentStatus(transactionId: String, paymentStatus: String) =
        viewModelScope.launch {
            try {
                val result = appRepositoryImpl.updatePaymentStatus(transactionId, paymentStatus)
                _updatePaymentStatusUiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _updatePaymentStatusUiState.value = UiState.Error(e.message.toString())
            }
        }

    fun updateDeliveryStatusById(transactionId: String) = viewModelScope.launch {
        try {
            val result =
                appRepositoryImpl.updateDeliveryStatusById(transactionId, "ready to pick up")
            _updateDeliveryStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updateDeliveryStatusUiState.value = UiState.Error(e.message.toString())
        }
    }
}