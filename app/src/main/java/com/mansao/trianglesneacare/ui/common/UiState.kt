package com.mansao.trianglesneacare.ui.common

import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.data.network.response.RegisterDriverResponse

sealed class UiState<out T : Any?> {
    object Standby : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val errorMessage: String) : UiState<Nothing>()

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
sealed interface DriverRegistrationUiState {
    object Standby : DriverRegistrationUiState
    object Loading : DriverRegistrationUiState
    data class Success(val registerResponse: RegisterDriverResponse) : DriverRegistrationUiState
    data class Error(val msg: String) : DriverRegistrationUiState
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


