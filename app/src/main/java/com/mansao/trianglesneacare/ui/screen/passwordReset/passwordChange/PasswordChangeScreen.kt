package com.mansao.trianglesneacare.ui.screen.passwordReset.passwordChange

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.screen.passwordReset.PasswordResetViewModel

@Composable
fun PasswordChangeScreen(
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit,
    sharedViewModel: SharedViewModel,
    passwordResetViewModel: PasswordResetViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { PasswordChangeTopBar(navigateBack = navigateBack) }
    ) {
        val context = LocalContext.current
        Surface(modifier = Modifier.padding(it)) {

            passwordResetViewModel.uiState.collectAsState().value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> {
                        LaunchedEffect(Unit) {
                            navigateToLogin()
                        }
                        Toast.makeText(context, uiState.data.msg, Toast.LENGTH_SHORT).show()
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

            PasswordChangeComponent(
                passwordResetViewModel = passwordResetViewModel,
                sharedViewModel = sharedViewModel
            )

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordChangeComponent(
    passwordResetViewModel: PasswordResetViewModel,
    sharedViewModel: SharedViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val email by remember { mutableStateOf(sharedViewModel.email) }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

        Spacer(modifier = Modifier.height(12.dp))
        PasswordChangeHeaderText(modifier = modifier)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    text = stringResource(
                        id = R.string.new_password
                    )
                )
            },
            maxLines = 1,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisibility) {
                            stringResource(R.string.hide_password)
                        } else {
                            stringResource(R.string.show_password)
                        }
                    )
                }
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                Text(
                    text = stringResource(
                        id = R.string.confirm_password
                    )
                )
            },
            maxLines = 1,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { passwordVisibility = !passwordVisibility },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisibility) {
                            stringResource(R.string.hide_password)
                        } else {
                            stringResource(R.string.show_password)
                        }
                    )
                }
            },
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    passwordResetViewModel.resetPassword(email, password, confirmPassword)
                }
            },
            enabled = password.isNotEmpty() && confirmPassword.isNotEmpty(),
            modifier = modifier
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun PasswordChangeHeaderText(modifier: Modifier= Modifier) {

    Spacer(modifier = Modifier.height(12.dp))
    Column {
        Text(
            text = "Please Enter New Password",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Enter your new password below and confirm it to reset your password.",
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(12.dp))
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