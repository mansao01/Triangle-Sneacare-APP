package com.mansao.trianglesneacare.ui.screen.section.service.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetServicesByCategoryIdResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GetServicesByCategoryIdResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetServicesByCategoryIdResponse>> = _uiState

    private var _role: MutableStateFlow<String> = MutableStateFlow("")
    val role: Flow<String> = _role

    init {
        getRole()
    }
    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }

    fun getServicesByCategoryId(categoryId: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.getServicesByCategory(categoryId)
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }

    private fun getRole() = viewModelScope.launch {
        val result = appRepositoryImpl.getRole()
        _role.value = result ?: ""
    }


}