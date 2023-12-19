package com.mansao.trianglesneacare.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.ui.common.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    var uiState: RegisterUiState by mutableStateOf(RegisterUiState.StandBy)
        private set

    fun getUiState() {
        uiState = RegisterUiState.StandBy
    }


    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            uiState = RegisterUiState.Loading
            uiState = try {
                val result = appRepositoryImpl.register(registerRequest)
                RegisterUiState.Success(result)
            } catch (e: Exception) {
                RegisterUiState.Error(e.toString())
            }
        }
    }
}