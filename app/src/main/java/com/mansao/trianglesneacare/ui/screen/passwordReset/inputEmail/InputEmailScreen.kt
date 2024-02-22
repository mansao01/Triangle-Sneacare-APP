package com.mansao.trianglesneacare.ui.screen.passwordReset.inputEmail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun InputEmailScreen(
    passwordResetViewModel: PasswordResetViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit,
    navigateToVerifyOTP: () -> Unit
) {

    val context = LocalContext.current

    Scaffold(topBar = { InputEmailTopBar(navigateBack = navigateBack) }) {
        Surface(modifier = Modifier.padding(it)) {
            passwordResetViewModel.uiState.collectAsState().value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> {
                        LaunchedEffect(Unit) {
                            Toast.makeText(
                                context,
                                uiState.data.msg,
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToVerifyOTP()
                        }

                    }

                    is UiState.Error -> LaunchedEffect(Unit) {
                        Toast.makeText(
                            context,
                            uiState.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            InputEmailComponent(
                passwordResetViewModel = passwordResetViewModel,
                sharedViewModel = sharedViewModel
            )
        }
    }

}


@Composable
fun InputEmailComponent(
    passwordResetViewModel: PasswordResetViewModel,
    sharedViewModel: SharedViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        val modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Please enter your email address below. We will send you an OTP through email to reset your password.",
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it.trim() },
            label = {
                Text(
                    text = stringResource(
                        id = R.string.email
                    )
                )
            },
            maxLines = 1,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (email.isNotEmpty()) {
                    passwordResetViewModel.sendResetPassword(email)
                    sharedViewModel.addEmail(email)
                }
            },
            enabled = email.isNotEmpty(),
            modifier = modifier
        ) {
            Text(text = "Next")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEmailTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = { /*TODO*/ },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })

}