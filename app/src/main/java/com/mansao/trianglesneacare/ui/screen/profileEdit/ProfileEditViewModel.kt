package com.mansao.trianglesneacare.ui.screen.profileEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetProfileDetailResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(private val appRepositoryIMpl: AppRepositoryImpl) :
    ViewModel() {

    private var _uiState: MutableStateFlow<UiState<GetProfileDetailResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: StateFlow<UiState<GetProfileDetailResponse>> = _uiState


    init {
        getDetailProfile()
    }

    private
    fun getDetailProfile() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val token = appRepositoryIMpl.getAccessToken()
                val result = appRepositoryIMpl.getProfileDetail("Bearer $token")
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }

    }

}