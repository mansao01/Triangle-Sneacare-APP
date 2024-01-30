package com.mansao.trianglesneacare.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.local.model.LocationModel
import com.mansao.trianglesneacare.data.network.response.PredictionsItem

class SharedViewModel : ViewModel() {
    var predictionItem by mutableStateOf<PredictionsItem?>(null)
        private set

    var location by mutableStateOf<LocationModel?>(null)

    fun addPlace(newPredictionItem: PredictionsItem) {
        predictionItem = newPredictionItem
    }

    fun addLocation(latitude: Double, longitude: Double) {
        location = LocationModel(latitude, longitude)
    }
}