package com.mansao.trianglesneacare.ui.screen.section.driver.pickUp.detail

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
class PickUpDetailViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _updateDeliveryStatusUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val updateDeliveryStatusUiState: Flow<UiState<OnlyMsgResponse>> = _updateDeliveryStatusUiState

    fun updateDeliveryStatusById(transactionId: String) = viewModelScope.launch {
        try {
            val result =
                appRepositoryImpl.updateDeliveryStatusById(transactionId, "already picked up")
            _updateDeliveryStatusUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _updateDeliveryStatusUiState.value = UiState.Error(e.message.toString())
        }
    }

}