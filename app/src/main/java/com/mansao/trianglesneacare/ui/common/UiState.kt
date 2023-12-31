package com.mansao.trianglesneacare.ui.common

import com.mansao.trianglesneacare.data.network.response.LoginResponse
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.data.network.response.RegisterResponse

sealed interface RegisterUiState {
    object StandBy : RegisterUiState
    object Loading : RegisterUiState
    data class Success(val registerResponse: RegisterResponse) : RegisterUiState
    data class Error(val msg: String) : RegisterUiState

}

sealed interface LoginUiState {
    object StandBy : LoginUiState
    object Loading : LoginUiState
    data class Success(val loginResponse: LoginResponse) : LoginUiState
    data class Error(val msg: String) : LoginUiState

}


sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(val profile: ProfileResponse) : ProfileUiState
    data class Error(val msg: String) : ProfileUiState

}

//admin section
sealed interface AdminHomeUiState {
    object Loading : AdminHomeUiState
    data class Success(val profile: ProfileResponse) : AdminHomeUiState
    data class Error(val msg: String) : AdminHomeUiState

}

sealed interface DriverManagementUiState {
    object StandBy : DriverManagementUiState
    object Loading : DriverManagementUiState
    data class Success(val registerResponse: RegisterResponse) : DriverManagementUiState
    data class Error(val msg: String) : DriverManagementUiState
}


sealed interface CustomerHomeUiState {
    object Loading : CustomerHomeUiState
    data class Success(val profile: ProfileResponse) : CustomerHomeUiState
    data class Error(val msg: String) : CustomerHomeUiState

}


sealed interface DriverHomeUiState {
    object Loading : DriverHomeUiState
    data class Success(val profile: ProfileResponse) : DriverHomeUiState
    data class Error(val msg: String) : DriverHomeUiState

}


