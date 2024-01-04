package com.mansao.trianglesneacare.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mansao.trianglesneacare.data.AppRepositoryImpl
import com.mansao.trianglesneacare.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val appRepositoryImpl: AppRepositoryImpl) :
    ViewModel() {
    private val _startDestination: MutableState<String> = mutableStateOf(Screen.Login.route)
    val startDestination: State<String> = _startDestination

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _loginState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loginState: StateFlow<Boolean> = _loginState

    private val _role: MutableState<String> = mutableStateOf("admin")
    val role: State<String> = _role

    init {
        viewModelScope.launch {
            appRepositoryImpl.getLoginState().collect() { isLogin ->
                _loginState.value = isLogin
                if (isLogin) {
                    _role.value = appRepositoryImpl.getRole().toString()
                    when (_role.value) {
                        "admin" -> _startDestination.value = Screen.AdminMain.route
                        "customer" -> _startDestination.value = Screen.CustomerMain.route
                        "driver" -> _startDestination.value = Screen.DriverMain.route
                    }
                } else {
                    _startDestination.value = Screen.Login.route
                }
            }
            _isLoading.value = false
        }
    }
}