package com.mansao.trianglesneacare.ui.screen.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.ui.common.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    var uiState: LoginUiState by mutableStateOf(LoginUiState.Loading)
        private set

    fun getUiState(){
        uiState = LoginUiState.StandBy
    }
}