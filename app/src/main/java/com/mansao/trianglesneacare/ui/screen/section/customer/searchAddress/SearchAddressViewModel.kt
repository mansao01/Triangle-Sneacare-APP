package com.mansao.trianglesneacare.ui.screen.section.customer.searchAddress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.AutoCompleteAddressResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAddressViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<AutoCompleteAddressResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<AutoCompleteAddressResponse>> = _uiState


    fun setLoadingState(){
        _uiState.value = UiState.Loading
    }

    fun setStandbyState(){
        _uiState.value = UiState.Standby
    }
    fun autoCompleteAddress(address: String) {
        viewModelScope.launch {
            try {
                val result = appRepositoryImpl.autoCompleteAddress(address)

                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }

}