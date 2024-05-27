package com.mansao.trianglesneacare.ui.screen.section.service.services.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateServiceViewModel @Inject constructor(private val appRepository: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<OnlyMsgResponse>> = _uiState

    private var _isDeleteSuccess = MutableStateFlow(false)
    val isDeleteSuccess: Flow<Boolean> = _isDeleteSuccess

    private var _deleteMessage = MutableStateFlow("")
    val deleteMessage: Flow<String> = _deleteMessage


    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }

    fun updateService(updateServiceRequest: UpdateServiceRequest) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepository.updateService(updateServiceRequest)
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }
    fun deleteService(serviceId: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepository.deleteService(serviceId)
            _isDeleteSuccess.value = true
            _deleteMessage.value = result.msg
        } catch (e: Exception) {
            _isDeleteSuccess.value = false
            _deleteMessage.value = e.message.toString()
        }
    }
}