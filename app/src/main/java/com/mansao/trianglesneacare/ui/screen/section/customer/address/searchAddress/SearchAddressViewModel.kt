package com.mansao.trianglesneacare.ui.screen.section.customer.address.searchAddress

import android.content.Context
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.AutoCompleteAddressResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAddressViewModel @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl,
    @ApplicationContext application: Context,
) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<AutoCompleteAddressResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<AutoCompleteAddressResponse>> = _uiState

    private var _gpsProviderState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val gpsProviderState: Flow<Boolean> = _gpsProviderState

    init {
        isGpsActive(application)
    }
    fun setLoadingState() {
        _uiState.value = UiState.Loading
    }

    fun setStandbyState() {
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

     fun isGpsActive(context: Context) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            _gpsProviderState.value = locationManager.isLocationEnabled
        }
    }


}