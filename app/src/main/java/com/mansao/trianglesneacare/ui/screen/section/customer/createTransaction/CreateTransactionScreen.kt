package com.mansao.trianglesneacare.ui.screen.section.customer.createTransaction

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.AddressItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AddressListItemSimple
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun CreateTransactionScreen(
    createTransactionViewModel: CreateTransactionViewModel = hiltViewModel(),
    navigateToAddAddress: () -> Unit,
    navigateBack: () -> Unit

) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        createTransactionViewModel.getCart()
        createTransactionViewModel.getAddresses()

    }
    Scaffold(topBar = {
        CreateTransactionTopBar(navigateBack = {navigateBack()})
    }) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            Column {
                AddressSection(
                    createTransactionViewModel = createTransactionViewModel,
                    context = context,
                    navigateToAddAddress = navigateToAddAddress
                )
            }

        }

    }

}

@Composable
fun AddressSection(
    createTransactionViewModel: CreateTransactionViewModel,
    context: Context,
    navigateToAddAddress: () -> Unit

) {
    createTransactionViewModel.addressUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {

            }

            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingScreen()
            is UiState.Success -> {
                AddressSectionContent(
                    addresses = uiState.data.address,
                    createTransactionViewModel = createTransactionViewModel,
                    navigateToAddAddress = navigateToAddAddress
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSectionContent(
    addresses: List<AddressItem>,
    createTransactionViewModel: CreateTransactionViewModel,
    navigateToAddAddress: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedAddressLabel by remember { mutableStateOf("") }
    var selectedAddressId by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = selectedAddressLabel,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            if (addresses.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "No address found. Please add an address.",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    },
                    onClick = {
                        navigateToAddAddress()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }else{
                addresses.forEach { addressItem ->
                    DropdownMenuItem(
                        text = {
                            AddressListItemSimple(address = addressItem)
                        },
                        onClick = {
                            selectedAddressLabel = addressItem.title
                            selectedAddressId = addressItem.id
                            isExpanded = false
                            createTransactionViewModel.calculateDistance("${addressItem.latitude},${addressItem.longitude}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                }
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add address.",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    },
                    onClick = {
                        navigateToAddAddress()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun CartItemsSection(
    modifier: Modifier = Modifier,
    createTransactionViewModel: CreateTransactionViewModel
) {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionTopBar(navigateBack: () -> Unit) {
    TopAppBar(title = { Text(text = "Checkout") },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null
                )
            }
        })

}
