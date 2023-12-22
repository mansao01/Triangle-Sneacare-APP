package com.mansao.trianglesneacare.ui.screen.profile

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
            uiState = ProfileUiState.Loading
            uiState = try {
                val token = appRepositoryImpl.getAccessToken()
                val profile = appRepositoryImpl.getProfile("Bearer $token")
                ProfileUiState.Success(profile)
            } catch (e: Exception) {
                ProfileUiState.Error(e.toString())
            }
        }
    }

}