package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.dto.Address
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun UpdateAddressScreen(
    updateAddressViewModel: UpdateAddressViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit
) {
    val addressId = sharedViewModel.addressId
    LaunchedEffect(Unit) {
        updateAddressViewModel.getDetailAddress(addressId)
    }
    val context = LocalContext.current


    Scaffold(
        topBar = {
            UpdateAddressTopBar(
                navigateBack = { navigateBack() },
                updateAddressViewModel = updateAddressViewModel,
                addressId = addressId
            )
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {

            updateAddressViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> UpdateAddressComponent(
                        address = uiState.data.address,
                        updateAddressViewModel = updateAddressViewModel,
                        addressId = addressId
                    )

                    is UiState.Error -> Toast.makeText(
                        context,
                        uiState.errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    updateAddressViewModel.actionSuccess.collectAsState(initial = false).value.let { isSuccess ->
        if (isSuccess) {
            navigateBack()
        }
    }

}

@Composable
fun UpdateAddressComponent(
    address: Address,
    updateAddressViewModel: UpdateAddressViewModel,
    addressId: Int
) {
    var fullAddress by remember { mutableStateOf(address.fullAddress ?: "") }
    var phone by remember { mutableStateOf(address.phone ?: "") }
    var receiverName by remember { mutableStateOf(address.receiverName ?: "") }
    var notes by remember { mutableStateOf(address.notes ?: "") }
    var addressLabel by remember { mutableStateOf(address.title ?: "") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Add some spacing at the top

        // Define a modifier for text fields and button
        val modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()

        // Address Label TextField
        OutlinedTextField(
            value = addressLabel,
            onValueChange = { addressLabel = it },
            label = { Text(text = stringResource(R.string.address_label)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = modifier.padding(top = 4.dp, bottom = 8.dp), // Add vertical padding
            shape = RoundedCornerShape(16.dp)
        )

        // Full Address TextField
        OutlinedTextField(
            value = fullAddress,
            onValueChange = { fullAddress = it },
            label = { Text(text = stringResource(R.string.full_address)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = modifier.padding(bottom = 8.dp), // Add bottom padding
            shape = RoundedCornerShape(16.dp)
        )

        // Receiver Name TextField
        OutlinedTextField(
            value = receiverName,
            onValueChange = { receiverName = it },
            label = { Text(text = stringResource(R.string.receiver_name)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = modifier.padding(bottom = 8.dp), // Add bottom padding
            shape = RoundedCornerShape(16.dp)
        )

        // Phone TextField
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(text = stringResource(R.string.phone)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = true,
            modifier = modifier.padding(bottom = 8.dp), // Add bottom padding
            shape = RoundedCornerShape(16.dp)
        )

        // Notes TextField
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text(text = stringResource(R.string.notes_to_courier)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = modifier.padding(bottom = 16.dp), // Add larger bottom padding
            shape = RoundedCornerShape(16.dp)
        )

        // Update Button
        Button(
            onClick = {
                updateAddress(
                    addressId,
                    receiverName,
                    fullAddress,
                    notes,
                    addressLabel,
                    phone,
                    updateAddressViewModel
                )
            },
            modifier = modifier.padding(bottom = 16.dp) // Add bottom padding
        ) {
            Text(text = stringResource(id = R.string.update))
        }
    }

}

private fun updateAddress(
    addressId: Int,
    receiverName: String,
    fullAddress: String,
    notes: String,
    addressLabel: String,
    phone: String,
    updateAddressViewModel: UpdateAddressViewModel
) {
    updateAddressViewModel.updateAddress(
        addressId,
        receiverName,
        fullAddress,
        notes,
        addressLabel,
        phone
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAddressTopBar(
    navigateBack: () -> Unit,
    updateAddressViewModel: UpdateAddressViewModel,
    addressId: Int
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.address_details)) },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            Text(
                text = stringResource(R.string.delete_address),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { updateAddressViewModel.deleteAddress(addressId) })
        }
    )

}