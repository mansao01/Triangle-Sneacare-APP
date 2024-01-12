package com.mansao.trianglesneacare.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
//    var uiState: LoginUiState by mutableStateOf(LoginUiState.StandBy)
//        private set

    private val _uiState: MutableStateFlow<UiState<LoginResponse>> =
        MutableStateFlow(UiState.Standby)
    val uiState: StateFlow<UiState<LoginResponse>> = _uiState


//    fun getUiState() {
//        uiState = LoginUiState.StandBy
//    }

    //    fun login(loginRequest: LoginRequest) {
//        viewModelScope.launch {
//            uiState = LoginUiState.Loading
//            uiState = try {
//                val result = appRepositoryImpl.login(loginRequest)
//                appRepositoryImpl.saveIsLoginState(true)
//                appRepositoryImpl.saveUsername(result.user.name)
//                result.user.role.role?.let { appRepositoryImpl.saveRole(it) }
//                appRepositoryImpl.saveAccessToken(result.accessToken)
//                LoginUiState.Success(result)
//            } catch (e: Exception) {
//                LoginUiState.Error(e.toString())
//            }
//        }
//    }
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
                result.user.role.role?.let { appRepositoryImpl.saveRole(it) }
                appRepositoryImpl.saveAccessToken(result.accessToken)
                _uiState.value = UiState.Success(result)
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "Network error occurred"
                    is HttpException -> {
                        when (e.code()) {
                            400 -> e.response()?.errorBody()?.string().toString()
                            // Add more cases for specific HTTP error codes if needed
                            else -> "HTTP error: ${e.code()}"
                        }
                    }
                    else -> "An unexpected error occurred"
                }
                _uiState.value = UiState.Error(errorMessage)
            }

        }
    }
}