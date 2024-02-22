package com.mansao.trianglesneacare.ui.screen.passwordReset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordResetViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _sendResetPasswordUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val sendResetPasswordUiState: StateFlow<UiState<OnlyMsgResponse>> = _sendResetPasswordUiState
    private fun setLoadingState() {
        _sendResetPasswordUiState.value = UiState.Loading
    }

    fun sendResetPassword(email: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.sendResetPassword(email)
            _sendResetPasswordUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _sendResetPasswordUiState.value = UiState.Error(e.message.toString())

        }
    }


    fun verifyOTP(email: String, otp: String) = viewModelScope.launch {
        try {
            val result = appRepositoryImpl.verifyOTP(email, otp)
            _sendResetPasswordUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _sendResetPasswordUiState.value = UiState.Error(e.message.toString())

        }
    }
}