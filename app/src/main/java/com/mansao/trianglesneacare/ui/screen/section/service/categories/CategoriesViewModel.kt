package com.mansao.trianglesneacare.ui.screen.section.service.categories

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
class CategoriesViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl):ViewModel() {
    private var _uiState: MutableStateFlow<UiState<GetCategoriesResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<GetCategoriesResponse>> = _uiState
    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }


     fun getCategories() = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.getCategories()
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }

    }
}