package com.mansao.trianglesneacare.ui.screen.section.customer.createTransaction

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
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
import com.mansao.trianglesneacare.data.network.response.CartItems
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AddressListItemSimple
import com.mansao.trianglesneacare.ui.components.CartListItemSimple
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun CreateTransactionScreen(
    createTransactionViewModel: CreateTransactionViewModel = hiltViewModel(),
    navigateToAddAddress: () -> Unit,
    navigateBack: () -> Unit,
    sharedViewModel: SharedViewModel

) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        createTransactionViewModel.getCart()
        createTransactionViewModel.getAddresses()
        createTransactionViewModel.setTotalPrice(sharedViewModel.totalPrice.toDouble())
    }
    val totalItems = createTransactionViewModel.totalItems.collectAsState(initial = 0).value
    val totalPrice = sharedViewModel.totalPrice
    Scaffold(topBar = {
        CreateTransactionTopBar(navigateBack = { navigateBack() })
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

                createTransactionViewModel.addressId.collectAsState(initial = "").value.let {

                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = it)

                    }
                    CartItemsSection(
                        createTransactionViewModel = createTransactionViewModel,
                        context = context
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailTransactionSection(
                        itemsCount = totalItems,
                        totalPrice = totalPrice,
                        createTransactionViewModel = createTransactionViewModel
                    )
                    CalculateDistance(
                        createTransactionViewModel = createTransactionViewModel,
                        context = context
                    )

                }

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
            } else {
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
                            createTransactionViewModel.setAddressId(addressItem.id)
                            Log.d("latLng", "${addressItem.latitude},${addressItem.longitude}")
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
    createTransactionViewModel: CreateTransactionViewModel,
    context: Context
) {
    createTransactionViewModel.cartUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingScreen()
            UiState.Standby -> {}
            is UiState.Success -> {
                CartItemSectionContent(cart = uiState.data.items)
                createTransactionViewModel.setTotalItem(uiState.data.items.size)
            }
        }
    }


}

@Composable
fun CartItemSectionContent(
    modifier: Modifier = Modifier, cart: List<CartItems>,
) {
    LazyColumn {
        items(cart) {
            CartListItemSimple(
                image = it.imageUrl,
                serviceName = it.service.serviceName,
                price = it.service.price
            )
        }
    }

}


@Composable
fun DetailTransactionSection(
    modifier: Modifier = Modifier,
    itemsCount: Int,
    totalPrice: Int,
    createTransactionViewModel: CreateTransactionViewModel
) {
    val totalShoppingPrice =
        createTransactionViewModel.totalShouldPay.collectAsState(initial = 0.0).value
    Column {
        Row {
            Text(text = "Total Price ($itemsCount items) ")
            Text(text = totalPrice.toString())
        }
        Row {
            Text(text = "Total shopping ")
            Text(text = totalShoppingPrice.toString())
        }

    }
}

@Composable
fun CalculateDistance(
    modifier: Modifier = Modifier,
    createTransactionViewModel: CreateTransactionViewModel,
    context: Context
) {
    createTransactionViewModel.calculateDistanceUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            is UiState.Success -> {
                val distanceText = uiState.data.data.rows[0].elements[0].distance.text
                createTransactionViewModel.setDistance(parseDistance(distanceText))
                createTransactionViewModel.calculateTotalTransaction()
                Log.d("distance", createTransactionViewModel.distance.collectAsState(initial = 0).value.toString())
            }

            UiState.Loading -> LoadingScreen()
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

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

fun parseDistance(distance: String): Double {
    // Remove the "km" part and trim any extra whitespace
    val numericPart = distance.replace(" km", "").trim()
    // Convert the resulting string to a Double
    return numericPart.toDouble()
}