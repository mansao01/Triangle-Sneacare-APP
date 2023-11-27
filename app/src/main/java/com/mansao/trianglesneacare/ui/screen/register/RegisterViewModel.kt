package com.mansao.trianglesneacare.ui.screen.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mansao.trianglesneacare.ui.common.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    var uiState: RegisterUiState by mutableStateOf(RegisterUiState.Loading)
        private set

    fun getUiState() {
        uiState = RegisterUiState.StandBy
    }
}