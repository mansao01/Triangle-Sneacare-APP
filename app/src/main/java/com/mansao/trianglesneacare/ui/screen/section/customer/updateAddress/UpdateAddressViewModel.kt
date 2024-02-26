package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.CustomerDetailAddressResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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

    private var _actionSuccess = MutableStateFlow(false)
    val actionSuccess:Flow<Boolean> = _actionSuccess
    private fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun getDetailAddress(addressId: Int) = viewModelScope.launch {
        setLoadingState()
        try {
            val token = appRepositoryImpl.getAccessToken()
            val result = appRepositoryImpl.getDetailCustomerAddresses("Bearer $token", addressId)
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }


    fun updateAddress(
        id: Int,
        receiverName: String,
        fullAddress: String,
        note: String,
        title: String,
        phone: String
    ) = viewModelScope.launch {
        setLoadingState()
        try {
            val token = appRepositoryImpl.getAccessToken()
            appRepositoryImpl.updateCustomerAddress(
                "Bearer $token",
                id,
                receiverName,
                fullAddress,
                note,
                title,
                phone
            )
            _actionSuccess.value = true
        } catch (e: Exception) {
            _actionSuccess.value = false


        }
    }


    fun deleteAddress(id: Int) {
        setLoadingState()
        viewModelScope.launch {
            try {
                val token = appRepositoryImpl.getAccessToken()
                appRepositoryImpl.deleteCustomerAddress("Bearer $token", id)
                _actionSuccess.value = true

            } catch (e: Exception) {
                _actionSuccess.value = false

            }
        }
    }
}