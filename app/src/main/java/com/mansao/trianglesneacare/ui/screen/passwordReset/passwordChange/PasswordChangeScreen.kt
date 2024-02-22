package com.mansao.trianglesneacare.ui.screen.passwordReset.passwordChange

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PasswordChangeScreen(
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { PasswordChangeTopBar(navigateBack = navigateBack) }
    ) {
        Surface(modifier = Modifier.padding(it)) {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordChangeTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
}