package com.mansao.trianglesneacare.ui.screen.section.admin.categories.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.AddCategoryRequest
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel()
class AddCategoryViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private var _uiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<OnlyMsgResponse>> = _uiState
    private fun setLoadingState() {
        _uiState.value = UiState.Loading

    }

    fun addCategory(itemName: String) = viewModelScope.launch {
        setLoadingState()
        try {
            val result = appRepositoryImpl.addCategory(AddCategoryRequest(itemName))
            _uiState.value = UiState.Success(result)
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e.message.toString())
        }
    }
}