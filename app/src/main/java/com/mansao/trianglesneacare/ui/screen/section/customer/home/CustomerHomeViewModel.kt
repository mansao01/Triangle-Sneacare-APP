package com.mansao.trianglesneacare.ui.screen.section.customer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetCategoriesResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerHomeViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GetCategoriesResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetCategoriesResponse>> = _uiState
    private var _username: MutableStateFlow<String> = MutableStateFlow("")
    val username: Flow<String> = _username

    init {
        getCategories()
        getCustomerName()
    }
    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }

    private fun getCategories() = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.getCategories()
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }

    private fun getCustomerName() = viewModelScope.launch {
        val result = appRepositoryImpl.getUsername()
        result?.let {
            _username.value = it
        }
    }
}