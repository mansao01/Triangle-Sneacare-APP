package com.mansao.trianglesneacare.ui.screen.section.customer.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GeocodingResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GeocodingResponse>> = _uiState

    fun getLocationFromPlaceId(placeId: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = appRepositoryImpl.geocodeWithPlaceId(placeId)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }
}