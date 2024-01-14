package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.ui.common.DriverRegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DriverRegistrationViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    var uiState: DriverRegistrationUiState by mutableStateOf(DriverRegistrationUiState.Standby)

    fun registerAsDriver(
        name: String,
        email: String,
        password: String,
        address: String,
        phone: String,
        file:File
    ) {
        viewModelScope.launch {
            uiState = DriverRegistrationUiState.Loading
            uiState = try {
                val result = appRepositoryImpl.registerDriver(name, email, password, address, phone, file)
                DriverRegistrationUiState.Success(result)
            } catch (e: Exception) {
                DriverRegistrationUiState.Error(e.toString())
            }
        }
    }
}