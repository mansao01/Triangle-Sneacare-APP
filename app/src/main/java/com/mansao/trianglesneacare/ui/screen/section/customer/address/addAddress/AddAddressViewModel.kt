package com.mansao.trianglesneacare.ui.screen.section.customer.address.addAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.CreateCustomerAddressRequest
import com.mansao.trianglesneacare.data.network.response.CreateCustomerAddressResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _uiState: MutableStateFlow<UiState<CreateCustomerAddressResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<CreateCustomerAddressResponse>> = _uiState


    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }

    fun createCustomerAddress(createCustomerAddressRequest: CreateCustomerAddressRequest) {
        setLoadingState()
        viewModelScope.launch {
            try {
                val token = appRepositoryImpl.getAccessToken()
                val result = appRepositoryImpl.createCustomerAddress(
                    "Bearer $token",
                    createCustomerAddressRequest
                )

                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }
}