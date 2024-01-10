package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
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
import com.mansao.trianglesneacare.ui.common.DriverRegistrationUiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.utils.CommonUtils

@Composable
fun DriverRegistrationScreen(
    driverRegistrationViewModel: DriverRegistrationViewModel = hiltViewModel(),
    navigateToDriverManagement: () -> Unit
) {
    val uiState = driverRegistrationViewModel.uiState
    DriverRegisterComponent(
        driverRegistrationViewModel = driverRegistrationViewModel,
        navigateToDriverManagement = { navigateToDriverManagement() },
        uiState = uiState
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DriverRegisterComponent(
    driverRegistrationViewModel: DriverRegistrationViewModel,
    navigateToDriverManagement: () -> Unit,
    uiState: DriverRegistrationUiState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var isLoading by remember { mutableStateOf(false) }
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current

    var name by remember { mutableStateOf("") }
    var isNameEmpty by remember { mutableStateOf(false) }

    var address by remember { mutableStateOf("") }
    var isAddressEmpty by remember { mutableStateOf(false) }

    var phone by remember { mutableStateOf("") }
    var isPhoneEmpty by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("") }
    var isEmailEmpty by remember { mutableStateOf(false) }

    var password by remember { mutableStateOf("") }
    var isPasswordEmpty by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    when (uiState) {
        is DriverRegistrationUiState.Standby -> {}
        is DriverRegistrationUiState.Loading -> {
            isLoading = true
        }

        is DriverRegistrationUiState.Success -> {
            LaunchedEffect(key1 = Unit) {
                snackbarHostState.showSnackbar(message = uiState.registerResponse.msg)
                isLoading = false
            }
        }

        is DriverRegistrationUiState.Error -> {
            LaunchedEffect(key1 = Unit) {
                snackbarHostState.showSnackbar(message = uiState.msg)
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { DriverRegistrationTopBar(navigateToDriverManagement = navigateToDriverManagement) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(R.string.name)) },
                    placeholder = { Text(text = stringResource(R.string.name)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    isError = isNameEmpty,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
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
                    isError = isEmailEmpty,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
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
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it.trim() },
                    label = { Text(text = stringResource(R.string.address)) },
                    placeholder = { Text(text = stringResource(R.string.address)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    isError = isNameEmpty,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.trim() },
                    label = { Text(text = stringResource(R.string.phone)) },
                    placeholder = { Text(text = stringResource(R.string.phone)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    isError = isNameEmpty,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                )

                OutlinedButton(

                    modifier = modifier
                        .padding(top = 18.dp)
                        .padding(end = 52.dp)
                        .align(Alignment.End)
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
                    onClick = {
                        when {
                            name.isEmpty() -> isNameEmpty = true
                            email.isEmpty() -> isEmailEmpty = true
                            password.isEmpty() -> isPasswordEmpty = true
                            address.isEmpty() -> isAddressEmpty = true
                            phone.isEmpty() -> isPhoneEmpty = true
                            else -> {
                                driverRegistrationViewModel.registerAsDriver(
                                    name,
                                    email,
                                    password,
                                    address,
                                    phone
                                )
                                isLoading = isLoading.not()
                            }
                        }
                    }) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    } else {
                        Text(stringResource(id = R.string.register))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverRegistrationTopBar(
    navigateToDriverManagement: () -> Unit
) {
    TopAppBar(title = { HeaderText(text = "Driver Register") },
        navigationIcon = {
            IconButton(
                onClick = { navigateToDriverManagement() }
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        })
}