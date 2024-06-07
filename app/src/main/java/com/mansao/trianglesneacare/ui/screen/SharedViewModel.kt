package com.mansao.trianglesneacare.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.data.local.model.LatLong
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.data.network.response.PredictionsItem
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem

class SharedViewModel : ViewModel() {
    var predictionItem by mutableStateOf<PredictionsItem?>(null)
        private set

    var location by mutableStateOf<LatLong?>(null)
    var fullAddress by mutableStateOf<String?>(null)
    var showSessionExpiredMessage by mutableStateOf(false)
    var addressId by mutableStateOf("")
    var email by mutableStateOf("")
    var categoryId by mutableStateOf("")
    var categoryName by mutableStateOf("")
    var serviceId by mutableStateOf("")
    var totalPrice by mutableIntStateOf(0)
    var updateServiceArgs by mutableStateOf<UpdateServiceRequest?>(null)
    var transactionId by mutableStateOf("")
    var snapToken by mutableStateOf("")
    var transactionItem by mutableStateOf<TransactionsItem?>(null)

    fun addTransactionItem(newTransactionItem: TransactionsItem) {
        transactionItem = newTransactionItem
    }

    fun addPlace(newPredictionItem: PredictionsItem) {
        predictionItem = newPredictionItem
    }

    fun addSnapToken(newSnapToken: String) {
        snapToken = newSnapToken
    }

    fun addTransactionId(newTransactionId: String) {
        transactionId = newTransactionId
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

    fun addCategoryName(newName: String) {
        categoryName = newName
    }

    fun addServiceId(newId: String) {
        serviceId = newId
    }

    fun addTotalPrice(newPrice: Int) {
        totalPrice = newPrice
    }


    fun addUpdateServiceArgs(newArgs: UpdateServiceRequest) {
        updateServiceArgs = newArgs
    }
}