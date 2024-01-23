package com.mansao.trianglesneacare.ui.screen.login
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.ForbiddenScreen
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.navigation.Screen
import com.mansao.trianglesneacare.ui.theme.Roboto
import com.mansao.trianglesneacare.utils.rememberImeState

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateToMain: () -> Unit,
    navigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current


    authViewModel.loginState.collectAsState().value.let { isLogin ->
        if (isLogin) {
            LoadingScreen()
        } else {
            LoginComponent(
                loginViewModel = loginViewModel,
                navigateToRegister = navigateToRegister
            )
            loginViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                when (uiState) {
                    is UiState.Loading -> LoadingScreen()
                    is UiState.Success -> {
                        navigateToMain()
                        Toast.makeText(
                            context,
                            "Welcome ${uiState.data.user.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Error -> {
                        loginViewModel.showDialog.collectAsState().value.let { showDialog ->
                            if (showDialog) {
                                ForbiddenScreen()
                            } else {
                                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    UiState.Standby -> {
                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginComponent(
    loginViewModel: LoginViewModel,
    navigateToRegister: () -> Unit
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    val isButtonEnable =
        (email.isNotEmpty() && password.isNotEmpty())


    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }
    Scaffold { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 30.dp)
            ) {
                Spacer(modifier = Modifier.height(46.dp))
                TopSection()
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
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
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)

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
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedButton(
                        enabled = isButtonEnable,
                        onClick = {
                            when {
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


}


@Composable
private fun TopSection() {
    Column(modifier = Modifier.fillMaxWidth()) {

        Icon(
            painter = painterResource(
                id = R.drawable.default_logo
            ),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        HeaderText(
            text = stringResource(R.string.greeting),
            description = stringResource(id = R.string.login_description),
            showDescription = true
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
    val navController: NavHostController = rememberNavController()
    LoginComponent(
        loginViewModel,
        navigateToRegister = { navController.navigate(Screen.Register.route) })
}