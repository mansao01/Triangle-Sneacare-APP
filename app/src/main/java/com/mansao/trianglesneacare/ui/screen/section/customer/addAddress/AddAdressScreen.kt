package com.mansao.trianglesneacare.ui.screen.section.customer.addAddress

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.request.CreateCustomerAddressRequest
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun AddAddressScreen(
    addAddressViewModel: AddAddressViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit,
    navigateToListAddress: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = { AddAddressTopBar(navigateBack = navigateBack) },
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            addAddressViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                AddAddressContent(
                    sharedViewModel = sharedViewModel,
                    addAddressViewModel = addAddressViewModel
                )
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> {
                        navigateToListAddress()
                    }

                    is UiState.Error -> Toast.makeText(
                        context,
                        uiState.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }
}

@Composable
fun AddAddressContent(
    sharedViewModel: SharedViewModel,
    addAddressViewModel: AddAddressViewModel
) {
    var addressLabel by rememberSaveable { mutableStateOf("") }
    var getFullAddress by rememberSaveable { mutableStateOf(sharedViewModel.fullAddress) }
    var fullAddress by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var receiverName by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    val isButtonEnable =
        ((addressLabel.isNotEmpty() || getFullAddress?.isNotEmpty() == true) && receiverName.isNotEmpty() && phone.isNotEmpty())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 4.dp)
    ) {
        OutlinedTextField(
            value = addressLabel,
            onValueChange = { addressLabel = it },
            label = { Text(text = stringResource(R.string.address_label)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onNext = {
                    KeyboardActions.Default.onNext
                }
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)

        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = getFullAddress ?: fullAddress,
            onValueChange = {
                if (getFullAddress?.isEmpty() == true) {
                    fullAddress = it
                } else {
                    getFullAddress = it
                }
            },
            label = { Text(text = stringResource(R.string.full_address)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    KeyboardActions.Default.onNext
                }
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)

        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = receiverName,
            onValueChange = {
                receiverName = it
            },
            label = { Text(text = stringResource(R.string.receiver_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    KeyboardActions.Default.onNext
                }
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
            },
            label = { Text(text = stringResource(R.string.notes_to_courier)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Note,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(
                onDone = {
                    KeyboardActions.Default.onNext
                }
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = { Text(text = "Phone Number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    KeyboardActions.Default.onNext
                }
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                addAddressViewModel.createCustomerAddress(
                    CreateCustomerAddressRequest(
                        title = addressLabel,
                        receiverName = receiverName,
                        phone = phone,
                        fullAddress = if (getFullAddress?.isNotEmpty() == true) getFullAddress else fullAddress,
                        note = notes
                    )
                )
            },
            enabled = isButtonEnable,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Text(text = stringResource(R.string.save))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressTopBar(
    navigateBack: () -> Unit

) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.detail_address)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        }
    )
}