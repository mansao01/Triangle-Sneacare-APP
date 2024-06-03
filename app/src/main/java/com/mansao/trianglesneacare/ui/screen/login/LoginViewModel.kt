package com.mansao.trianglesneacare.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appRepositoryImpl: AppRepositoryImpl
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<LoginResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: StateFlow<UiState<LoginResponse>> = _uiState

    private val _showDialog: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private fun setLoadingState() {
        _uiState.value = UiState.Loading
    }

    fun login(loginRequest: LoginRequest) {
        setLoadingState()
        viewModelScope.launch {
            try {
                val result = appRepositoryImpl.login(loginRequest)
                appRepositoryImpl.saveIsLoginState(true)
                appRepositoryImpl.saveUsername(result.user.name)
                appRepositoryImpl.saveUserEmail(result.user.email)
                appRepositoryImpl.saveUserId(result.user.id)
                result.user.role.role?.let { appRepositoryImpl.saveRole(it) }
                appRepositoryImpl.saveAccessToken(result.accessToken)
                appRepositoryImpl.saveRefreshToken(result.refreshToken)
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
                            else ->  """
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
                if (msg.toString() == "Please verify your email first, check your email"){
                    _showDialog.value = true
                    _uiState.value = UiState.Error(msg.toString())

                }else{
                    _uiState.value = UiState.Error(msg.toString())

                }

            }
        }
    }
}