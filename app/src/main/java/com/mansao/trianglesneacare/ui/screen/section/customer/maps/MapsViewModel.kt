package com.mansao.trianglesneacare.ui.screen.section.customer.maps

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GeocodingResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GeocodingResponse>> = _uiState

    private val _address: MutableStateFlow<String> =
        MutableStateFlow("")
    val address: StateFlow<String> = _address


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

    fun getDetailLocation(latitude: Double, longitude: Double, context: Context) {
        viewModelScope.launch {
            val geocoder = Geocoder(context, Locale.getDefault())


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    latitude, longitude, 1
                ) { addresses ->
                    val address = addresses[0]
                    val addressText = address.getAddressLine(0) ?: ""


                    _address.value =addressText

                }
            } else {
                try {
                    val addresses: MutableList<Address>? =
                        geocoder.getFromLocation(latitude, longitude, 1)
                    if (addresses!!.isNotEmpty()) {
                        val address = addresses[0]
                        Log.d("full location", address.toString())
                        val addressText = address.getAddressLine(0) ?: ""

                        _address.value = addressText
                    } else {
                        _address.value = ""

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }

}