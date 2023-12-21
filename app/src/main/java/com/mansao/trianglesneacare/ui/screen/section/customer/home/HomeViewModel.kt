package com.mansao.trianglesneacare.ui.screen.section.customer.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.ui.common.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getProfile()
    }
    private fun getProfile() {
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val token = appRepositoryImpl.getAccessToken()
                val profile = appRepositoryImpl.getProfile("Bearer $token")
                HomeUiState.Success(profile)
            } catch (e: Exception) {

                HomeUiState.Error(e.toString())
            }
        }
    }
}