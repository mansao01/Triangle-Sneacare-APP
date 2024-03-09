package com.mansao.trianglesneacare.ui.screen.passwordReset.otpVerification

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.screen.passwordReset.PasswordResetViewModel

@Composable
fun OTPVerificationScreen(
    sharedViewModel: SharedViewModel,
    passwordResetViewModel: PasswordResetViewModel = hiltViewModel(),
    navigateToPasswordChange: () -> Unit,
    navigateBack: () -> Unit
) {
    passwordResetViewModel.setStandByState()
    val context = LocalContext.current
    Scaffold(topBar = { OTPVerificationTopBar(navigateBack = navigateBack) }) {
        Surface(modifier = Modifier.padding(it)) {
            passwordResetViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> {
                        LaunchedEffect(Unit) {
                            navigateToPasswordChange()
                        }
                    }

                    is UiState.Error -> {
                        LaunchedEffect(Unit) {
                            Toast.makeText(
                                context,
                                uiState.errorMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            OTPVerificationComponent(
                sharedViewModel = sharedViewModel,
                passwordResetViewModel = passwordResetViewModel
            )
        }
    }

}

@Composable
fun OTPVerificationComponent(
    sharedViewModel: SharedViewModel,
    passwordResetViewModel: PasswordResetViewModel

) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        Text(
            text = stringResource(R.string.reset_password_title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.reset_password_description),
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(22.dp))
        OTPTextField(
            sharedViewModel = sharedViewModel,
            passwordResetViewModel = passwordResetViewModel,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }
}

@Composable
fun OTPTextField(
    sharedViewModel: SharedViewModel,
    passwordResetViewModel: PasswordResetViewModel,
    modifier: Modifier = Modifier
) {
    var otpText by remember {
        mutableStateOf("")
    }
    val email by remember { mutableStateOf(sharedViewModel.email) }
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    BasicTextField(
        value = otpText,
        onValueChange = {
            if (it.length <= 6) {
                otpText = it.uppercase()
            }
        },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(6) { index ->
                val number = when {
                    index >= otpText.length -> ""
                    else -> otpText[index]
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = number.toString(), style = MaterialTheme.typography.titleLarge)
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(uiColor)
                    )
                }
            }

        }
    }

    LaunchedEffect(otpText.length) {
        if (otpText.length == 6) {
            passwordResetViewModel.verifyOTP(email, otpText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
}