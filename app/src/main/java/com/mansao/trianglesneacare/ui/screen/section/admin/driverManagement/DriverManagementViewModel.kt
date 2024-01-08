package com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.ui.common.DriverManagementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverManagementViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    var uiState: DriverManagementUiState by mutableStateOf(DriverManagementUiState.Loading)
        private set

    init {
        getDrivers()
    }
    private fun getDrivers() {
        viewModelScope.launch {
            uiState = DriverManagementUiState.Loading
            uiState = try {
                val result = appRepositoryImpl.getDrivers()
                DriverManagementUiState.Success(result)
            } catch (e: Exception) {
                DriverManagementUiState.Error(e.toString())
            }
        }
    }
}