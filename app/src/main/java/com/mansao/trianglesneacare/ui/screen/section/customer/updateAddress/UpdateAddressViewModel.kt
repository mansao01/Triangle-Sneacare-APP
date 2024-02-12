package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.CustomerDetailAddressResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateAddressViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<CustomerDetailAddressResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: StateFlow<UiState<CustomerDetailAddressResponse>> = _uiState

    private var _deleteMessage: MutableStateFlow<String> =
        MutableStateFlow("")
    val deleteMessage: StateFlow<String> = _deleteMessage

    private var _updateMessage: MutableStateFlow<String> =
        MutableStateFlow("")
    val updateMessage: StateFlow<String> = _updateMessage


    fun getDetailAddress(addressId: Int) {
        viewModelScope.launch {
            val token = appRepositoryImpl.getAccessToken()
            val result = appRepositoryImpl.getDetailCustomerAddresses("Bearer $token", addressId)
        }
    }

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
                _updateMessage.value = result.msg
            } catch (e: Exception) {
                _updateMessage.value = e.message.toString()

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