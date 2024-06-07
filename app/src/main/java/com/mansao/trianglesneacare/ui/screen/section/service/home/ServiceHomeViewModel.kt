package com.mansao.trianglesneacare.ui.screen.section.service.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetTransactionByDeliveryStatusResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServiceHomeViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl):ViewModel() {

    private var _uiState: MutableStateFlow<UiState<GetTransactionByDeliveryStatusResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetTransactionByDeliveryStatusResponse>> = _uiState
    fun getTransactionWhereAlreadyPickedUp() = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.getTransactionByDeliveryStatus("already picked up")
            _uiState.value = UiState.Success(result)
        }catch (e:Exception){
            _uiState.value = UiState.Error(e.message.toString())

        }
    }

}