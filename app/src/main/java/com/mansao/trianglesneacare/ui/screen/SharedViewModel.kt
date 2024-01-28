package com.mansao.trianglesneacare.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.network.response.PredictionsItem

class SharedViewModel : ViewModel() {
    var predictionItem by mutableStateOf<PredictionsItem?>(null)
        private set

    fun addPlace(newPredictionItem: PredictionsItem) {
        predictionItem = newPredictionItem
    }
}