package com.mansao.trianglesneacare.ui.screen.register

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.EmailSentDialog
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.utils.CommonUtils
import com.mansao.trianglesneacare.utils.rememberImeState

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    RegisterComponent(
        registerViewModel = registerViewModel,
        isLoading = isLoading,
        navigateBack = navigateBack,
        modifier = modifier,
    )

    registerViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Standby -> {}
            is UiState.Loading -> isLoading = true
            is UiState.Success -> {
                EmailSentDialog(
                    navigateBack = { navigateBack() },
                    email = uiState.data.user.email
                )
                isLoading = false
            }

            is UiState.Error -> {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterComponent(
    registerViewModel: RegisterViewModel,
    isLoading: Boolean,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    val isButtonEnable =
        (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty())

    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    Scaffold(
        topBar = { RegisterTopBar(navigateBack = navigateBack) }
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            Column(
                modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 30.dp)
            ) {
                HeaderText(
                    text = stringResource(R.string.greeting),
                    description = stringResource(R.string.register_description),
                    showDescription = true
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.trim() },
                    label = { Text(text = stringResource(R.string.name)) },
                    placeholder = { Text(text = stringResource(R.string.name)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp)

                )

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
                            if (CommonUtils.isEmailValid(email)) {
                                keyboardController?.hide()
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
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
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp)

                )

                OutlinedButton(
                    onClick = {
                        when {
                            else -> {
                                registerViewModel.register(
                                    RegisterRequest(
                                        name,
                                        email,
                                        password,
                                    )
                                )
                            }
                        }
                    },
                    modifier = modifier
                        .padding(top = 18.dp)
                        .fillMaxWidth()
                        .then(
                            if (buttonSize != DpSize.Zero) Modifier.size(buttonSize) else Modifier
                        )
                        .onSizeChanged { newSize ->
                            if (buttonSize == DpSize.Zero) {
                                buttonSize = with(density) {
                                    newSize
                                        .toSize()
                                        .toDpSize()
                                }
                            }
                        },
                    enabled = isButtonEnable
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    } else {
                        Text(text = stringResource(id = R.string.register))
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(title = { }, navigationIcon = {
        IconButton(onClick = { navigateBack() }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
    })

}


