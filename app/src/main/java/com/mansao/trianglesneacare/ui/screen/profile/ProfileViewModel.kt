package com.mansao.trianglesneacare.ui.screen.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.ui.common.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    var uiState: ProfileUiState by mutableStateOf(ProfileUiState.Loading)
        private set

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            val refreshToken = appRepositoryImpl.getRefreshToken()
            Log.d("refresh token", refreshToken.toString())
            val updatedRefreshToken = refreshToken?.let { appRepositoryImpl.refreshToken(it) }
            updatedRefreshToken?.accessToken?.let { appRepositoryImpl.saveAccessToken(it) }
            uiState = ProfileUiState.Loading
            uiState = try {
                val token = appRepositoryImpl.getAccessToken()
                Log.d("access token", token.toString())
                val profile = appRepositoryImpl.getProfile("Bearer $token")
                ProfileUiState.Success(profile)
            } catch (e: Exception) {
                ProfileUiState.Error(e.toString())
            }
        }
    }

    fun logout() {
        uiState = ProfileUiState.Loading
        viewModelScope.launch {
            appRepositoryImpl.clearToken()
            appRepositoryImpl.clearRoleName()
            appRepositoryImpl.clearUsername()
            appRepositoryImpl.saveIsLoginState(false)
            appRepositoryImpl.saveShowBalloonState(true)

        }
    }

}