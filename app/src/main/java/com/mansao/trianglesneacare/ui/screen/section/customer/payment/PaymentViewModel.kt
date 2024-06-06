package com.mansao.trianglesneacare.ui.screen.section.customer.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetPaymentStatusResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _getOnlinePaymentStatusUiState: MutableStateFlow<UiState<GetPaymentStatusResponse>> =
        MutableStateFlow(UiState.Standby)
    val getOnlinePaymentStatusUiState: Flow<UiState<GetPaymentStatusResponse>> =
        _getOnlinePaymentStatusUiState
    fun getPaymentStatus(transactionId: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.getPaymentStatus(transactionId)
            _getOnlinePaymentStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _getOnlinePaymentStatusUiState.value = UiState.Error(e.message.toString())

        }
    }
}

