package com.mansao.trianglesneacare.ui.screen.section.service.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.GetServicesByCategoryIdResponse
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _getServiceByCategoryUiState: MutableStateFlow<UiState<GetServicesByCategoryIdResponse>> =
        MutableStateFlow(UiState.Standby)
    val getServiceByCategoryUiState = _getServiceByCategoryUiState.asStateFlow()

    private var _deleteCategoryUiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val deleteCategoryUiState = _deleteCategoryUiState.asStateFlow()

    private var _role: MutableStateFlow<String> = MutableStateFlow("")
    val role: Flow<String> = _role

    init {
        getRole()
    }

    private fun setLoadingState() {
        _getServiceByCategoryUiState.value = UiState.Loading
    }

    fun getServicesByCategoryId(categoryId: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.getServicesByCategory(categoryId)
            _getServiceByCategoryUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _getServiceByCategoryUiState.value = UiState.Error(e.message.toString())
        }
    }

     fun deleteCategory(categoryId: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.deleteCategory(categoryId)
            _deleteCategoryUiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _deleteCategoryUiState.value = UiState.Error(e.message.toString())
        }
    }

    private fun getRole() = viewModelScope.launch {
        val result = appRepositoryImpl.getRole()
        _role.value = result ?: ""
    }


}