package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.request.DriverRegisterRequest
import com.mansao.trianglesneacare.data.network.request.RegisterRequest
import com.mansao.trianglesneacare.ui.common.DriverRegistrationUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.register.RegisterViewModel
import com.mansao.trianglesneacare.utils.CommonUtils

@Composable
fun DriverRegistrationScreen(
    uiState: DriverRegistrationUiState,
    driverRegistrationViewModel: DriverRegistrationViewModel = hiltViewModel(),
    navigateToDriverManagement: () -> Unit
) {
    val context = LocalContext.current
    when (uiState) {
        is DriverRegistrationUiState.Standby -> DriverRegisterComponent(driverRegistrationViewModel = driverRegistrationViewModel)
        is DriverRegistrationUiState.Loading -> LoadingScreen()
        is DriverRegistrationUiState.Success -> {
            navigateToDriverManagement()
            Toast.makeText(context, uiState.registerResponse.msg, Toast.LENGTH_SHORT).show()
        }

        is DriverRegistrationUiState.Error -> Toast.makeText(
            context,
            uiState.msg,
            Toast.LENGTH_SHORT
        ).show()
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DriverRegisterComponent(
    driverRegistrationViewModel: DriverRegistrationViewModel,
    modifier: Modifier = Modifier
) {
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

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.register),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 48.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = stringResource(R.string.name)) },
            placeholder = { Text(text = stringResource(R.string.name)) },
            leadingIcon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) },
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
            leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) },
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
            leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) },
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
            leadingIcon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = null) },
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
            leadingIcon = { Icon(imageVector = Icons.Outlined.Phone, contentDescription = null) },
            singleLine = true,
            isError = isNameEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )

        Button(
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
                    }
                }
            },
            modifier = Modifier
                .padding(top = 18.dp)
                .padding(end = 52.dp)
                .align(Alignment.End)
        ) {
            Text(text = "Register")
        }
    }
}