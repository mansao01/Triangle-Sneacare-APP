package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.DriverRegisterRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.ui.common.DriverRegistrationUiState
import com.mansao.trianglesneacare.ui.common.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverRegistrationViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    var uiState: DriverRegistrationUiState by mutableStateOf(DriverRegistrationUiState.Standby)
        private set

    fun registerAsDriver(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
    ) {
        viewModelScope.launch {
            uiState = DriverRegistrationUiState.Loading
            uiState = try {
                val result = appRepositoryImpl.registerDriver(name, email, password, address, phone)
                DriverRegistrationUiState.Success(result)
            } catch (e: Exception) {
                DriverRegistrationUiState.Error(e.toString())
            }
        }
    }
}