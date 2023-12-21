package com.mansao.trianglesneacare.ui.screen.section.customer.home

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.ui.common.HomeUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun CustomerHomeScreen(
    uiState: HomeUiState
) {
    val context = LocalContext.current
    when (uiState) {
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> Text(text = uiState.profile.profile.toString())
        is HomeUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
    }

}