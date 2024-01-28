package com.mansao.trianglesneacare.ui.screen.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.response.OnlyMsgResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {

    private var _uiState: MutableStateFlow<UiState<OnlyMsgResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: Flow<UiState<OnlyMsgResponse>> = _uiState


    init {
        checkServerRunning()
    }
    private fun setLoadingState() {
        _uiState.value = UiState.Loading
    }
     fun checkServerRunning() {
        setLoadingState()
        viewModelScope.launch {
            try {
                val result = appRepositoryImpl.checkServerIsRunning()
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> """
                        {"msg": "Service unavailable"}
                    """.trimIndent()

                    is HttpException -> {
                        when (e.code()) {
                            400 -> e.response()?.errorBody()?.string().toString()
                            // Add more cases for specific HTTP error codes if needed
                            else -> """
                                {"msg": "Error code ${e.message()}"}
                            """.trimIndent()
                        }
                    }

                    else -> """
                        {"msg": "Service unavailable"}
                    """.trimIndent()
                }
                val gson = Gson()
                val jsonObject = gson.fromJson(errorMessage, Map::class.java) as Map<*, *>
                val msg = jsonObject["msg"]
                _uiState.value = UiState.Error(msg.toString())
            }
        }
    }
}