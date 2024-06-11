package com.mansao.trianglesneacare.ui.screen.section.owner.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetTransactionByMonthResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class OwnerHomeViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GetTransactionByMonthResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetTransactionByMonthResponse>> = _uiState

    init {
        getAllTransaction()
    }
    private fun setLoadingState(){
        _uiState.value = UiState.Loading
    }
    fun getAllTransaction() = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.getAllTransaction()

            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTransactionByMonth() = viewModelScope.launch {
        setLoadingState()
        try {
            val currentDate = LocalDate.now()

            // Extract the month and year
            val currentMonth = currentDate.monthValue
            val currentYear = currentDate.year
            val result = appRepositoryImpl.getTransactionByMonth(currentMonth, currentYear)

            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTransactionByMonthAndPaymentStatus(paymentStatus:String) = viewModelScope.launch {
        setLoadingState()
        try {
            val currentDate = LocalDate.now()

            // Extract the month and year
            val currentMonth = currentDate.monthValue
            val currentYear = currentDate.year
            val result = appRepositoryImpl.getTransactionByMonthAndPaymentStatus(
                currentMonth,
                currentYear,
                paymentStatus
            )

            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }


}