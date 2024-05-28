package com.mansao.trianglesneacare.ui.screen.section.customer.address.addressList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetCustomerAddressesResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressListViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GetCustomerAddressesResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetCustomerAddressesResponse>> = _uiState

    private fun setToLoadingState(){
        _uiState.value = UiState.Loading
    }

     fun getAddresses(){
        setToLoadingState()
        viewModelScope.launch {
          try {
              val token = appRepositoryImpl.getAccessToken()
              val result =appRepositoryImpl.getCustomerAddresses("Bearer $token")
              _uiState.value = UiState.Success(result)

          }catch (e:Exception){
              _uiState.value = UiState.Error(e.message.toString())
          }

        }
    }
}