package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAddressViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: StateFlow<UiState<OnlyMsgResponse>> = _uiState

    private var _deleteMessage: MutableStateFlow<String> =
        MutableStateFlow("")
    val deleteMessage: StateFlow<String> = _deleteMessage

    fun updateAddress(
        id: Int,
        receiverName: String,
        fullAddress: String,
        note: String
    ) {
        viewModelScope.launch {
            try {
                val token = appRepositoryImpl.getAccessToken()
                val result = appRepositoryImpl.updateCustomerAddress(
                    "Bearer $token",
                    id,
                    receiverName,
                    fullAddress,
                    note
                )
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())

            }
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            try {
                val token = appRepositoryImpl.getAccessToken()
                val result = appRepositoryImpl.deleteCustomerAddress("Bearer $token", id)
                _deleteMessage.value = result.msg
            } catch (e: Exception) {
                _deleteMessage.value = e.message.toString()
            }
        }
    }
}