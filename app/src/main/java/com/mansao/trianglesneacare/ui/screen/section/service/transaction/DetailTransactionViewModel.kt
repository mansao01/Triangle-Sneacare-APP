package com.mansao.trianglesneacare.ui.screen.section.service.transaction

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
class DetailTransactionViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {


    private var _updateDeliveryStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updateDeliveryStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updateDeliveryStatusUiState

    private var _updateWashStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updateWashStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updateWashStatusUiState

    private var _sendFinishEmailUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val sendFinishEmailUiState: Flow<UiState<OnlyMsgResponse>> = _sendFinishEmailUiState

    fun updateDeliveryStatusById(transactionId: String, status:String) = viewModelScope.launch {
        _updateDeliveryStatusUiState.value = UiState.Loading
        try {
            val result =
                appRepositoryImpl.updateDeliveryStatusById(
                    transactionId,
                    status
//                    "ready to deliver"
                )
            _updateDeliveryStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updateDeliveryStatusUiState.value = UiState.Error(e.message.toString())
        }
    }

    fun updateWashStatus(orderId: String, washStatus: String) = viewModelScope.launch {
        _updateWashStatusUiState.value = UiState.Loading
        try {
            val result =
                appRepositoryImpl.updateWashStatus(orderId, washStatus)
            _updateWashStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updateWashStatusUiState.value = UiState.Error(e.message.toString())
        }
    }

    fun sendFinishEmail() = viewModelScope.launch {
        _sendFinishEmailUiState.value = UiState.Loading
        try {
            val email = appRepositoryImpl.getUserEmail() ?: ""
            val username = appRepositoryImpl.getUsername() ?: ""
            val result =
                appRepositoryImpl.updateWashStatus(email, username)
            _sendFinishEmailUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _sendFinishEmailUiState.value = UiState.Error(e.message.toString())
        }
    }

}