package com.mansao.trianglesneacare.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.local.model.LatLong
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.data.network.response.PredictionsItem

class SharedViewModel : ViewModel() {
    var predictionItem by mutableStateOf<PredictionsItem?>(null)
        private set

    var location by mutableStateOf<LatLong?>(null)
    var fullAddress by mutableStateOf<String?>(null)
    var showSessionExpiredMessage by mutableStateOf(false)
    var addressId by mutableStateOf("")
    var email by mutableStateOf("")
    var categoryId by mutableStateOf("")
    var updateServiceArgs by mutableStateOf<UpdateServiceRequest?>(null)

    fun addPlace(newPredictionItem: PredictionsItem) {
        predictionItem = newPredictionItem
    }

    fun addLocation(latitude: Double, longitude: Double) {
        location = LatLong(latitude, longitude)
    }

    fun addFullAddress(newAddress: String) {
        fullAddress = newAddress

    }

    fun changeSessionExpiredState(isExpired: Boolean) {
        showSessionExpiredMessage = isExpired

    }

    fun addEmail(newEmail: String) {
        email = newEmail
    }

    fun addAddressId(newId: String) {
        addressId = newId
    }

    fun addCategoryId(newId: String) {
        categoryId = newId
    }



    fun addUpdateServiceArgs(newArgs: UpdateServiceRequest) {
        updateServiceArgs = newArgs
    }
}