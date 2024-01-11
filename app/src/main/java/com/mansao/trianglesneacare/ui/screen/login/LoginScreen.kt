package com.mansao.trianglesneacare.ui.screen.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.request.LoginRequest
import com.mansao.trianglesneacare.ui.AuthViewModel
import com.mansao.trianglesneacare.ui.common.LoginUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.theme.Roboto

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToMain: () -> Unit,
    navigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isLogin by authViewModel.loginState.collectAsState()

    if (isLogin) {
        LoadingScreen()
    } else {
        when (uiState) {
            is LoginUiState.StandBy -> LoginComponentNew(
                loginViewModel = loginViewModel,
                navigateToRegister = navigateToRegister
            )

            is LoginUiState.Loading -> LoadingScreen()
            is LoginUiState.Success -> {
                LaunchedEffect(Unit) {

                    Toast.makeText(
                        context,
                        "Welcome ${uiState.loginResponse.user.name}",
                        Toast.LENGTH_LONG
                    ).show()

                    navigateToMain()
                }

                Log.d("LoginScreen", uiState.loginResponse.accessToken)
            }

            is LoginUiState.Error -> {
                Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
                loginViewModel.getUiState()
            }
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginComponentNew(
    loginViewModel: LoginViewModel,
    navigateToRegister: () -> Unit
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    var email by remember { mutableStateOf("") }
    var isEmailEmpty by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var isPasswordEmpty by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection(uiColor)
            Spacer(modifier = Modifier.height(26.dp))
            Column(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text(text = stringResource(R.string.enter_your_email)) },
                    placeholder = { Text(text = stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isEmailValid(email)) {
                                keyboardController?.hide()
                            }
                        }
                    ),
                    isError = isEmailEmpty,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()

                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = stringResource(R.string.enter_your_password)) },
                    placeholder = { Text(text = stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = isPasswordEmpty,
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                Button(
                    onClick = {
                        when {
                            email.isEmpty() -> isEmailEmpty = true
                            password.isEmpty() -> isPasswordEmpty = true
                            else -> {
                                loginViewModel.login(LoginRequest(email, password))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }

                RegisterText(
                    uiColor = uiColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .clickable { navigateToRegister() })
            }
        }
    }


}

@Composable
private fun TopSection(
    uiColor: Color
) {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.46f)
        )

        Icon(
            painter = painterResource(
                id = R.drawable.default_logo
            ),
            contentDescription = null,
            modifier = Modifier.padding(top = 80.dp)
        )
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor,
            modifier = Modifier
                .padding(bottom = 10.dp, top = 280.dp)

        )
    }
}

@Composable
fun RegisterText(
    uiColor: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF94A3BB),
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append("Don't have account?")
            }
            withStyle(
                style = SpanStyle(
                    color = uiColor,
                    fontSize = 14.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append("")
                append("Create now")
            }
        },
        modifier = modifier
    )
}

private fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showSystemUi = true)
@Composable
fun NewLoginComponentPreview() {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val navController:NavHostController = rememberNavController()
    LoginComponentNew(loginViewModel, navigateToRegister = {navController.navigate(Screen.Register.route)})
}