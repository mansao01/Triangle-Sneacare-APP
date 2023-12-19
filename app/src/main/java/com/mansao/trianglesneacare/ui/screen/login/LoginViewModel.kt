package com.mansao.trianglesneacare.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.ui.common.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.StandBy)
        private set

    fun getUiState() {
        uiState = LoginUiState.StandBy
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            uiState = LoginUiState.Loading
            uiState = try {
                val result = appRepositoryImpl.login(loginRequest)
                LoginUiState.Success(result)
            } catch (e: Exception) {
                LoginUiState.Error(e.toString())
            }
        }
    }
}