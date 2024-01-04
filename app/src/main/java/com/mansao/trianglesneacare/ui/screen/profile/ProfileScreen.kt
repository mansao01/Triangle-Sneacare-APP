package com.mansao.trianglesneacare.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.ui.common.ProfileUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    when (uiState) {
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Success -> ProfileComponent(
            profile = uiState.profile,
            viewModel = profileViewModel
        )

        is ProfileUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ProfileComponent(
    profile: ProfileResponse,
    viewModel: ProfileViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = profile.toString())
        Button(onClick = { viewModel.logout() }) {
            Text(text = "logout")
        }
    }
}