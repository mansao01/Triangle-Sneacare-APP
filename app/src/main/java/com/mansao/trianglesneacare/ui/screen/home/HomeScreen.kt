package com.mansao.trianglesneacare.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mansao.trianglesneacare.ui.common.HomeUiState
import com.mansao.trianglesneacare.ui.common.LoginUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.login.LoginComponent

@Composable
fun HomeScreen(
    uiState: HomeUiState
) {
    when (uiState) {

        is HomeUiState.Loading -> LoadingScreen()
//        is HomeUiState.Success -> {
//
//        }

        is HomeUiState.Error -> {
        }
    }
}