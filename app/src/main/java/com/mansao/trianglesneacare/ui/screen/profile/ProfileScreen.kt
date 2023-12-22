package com.mansao.trianglesneacare.ui.screen.profile

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mansao.trianglesneacare.ui.common.ProfileUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun ProfileScreen(
    uiState: ProfileUiState
) {
    val context = LocalContext.current
    when (uiState) {
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Success -> Text(text = uiState.profile.profile.toString())
        is ProfileUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
    }
}