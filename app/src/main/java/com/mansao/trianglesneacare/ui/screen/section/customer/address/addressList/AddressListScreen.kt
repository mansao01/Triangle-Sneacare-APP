package com.mansao.trianglesneacare.ui.screen.section.customer.address.addressList

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.AddressItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AddressListItem
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun AddressListScreen(
    navigateBack: () -> Unit,
    navigateToEditAddress: () -> Unit,
    navigateToSearchAddress: () -> Unit,
    viewModel: AddressListViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel

) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAddresses()
    }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AddressListTopBar(
                navigateBack = navigateBack,
                navigateToSearchAddress = navigateToSearchAddress
            )
        }
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            viewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> AddressListComponent(
                        address = uiState.data.address,
                        navigateToEditAddress = navigateToEditAddress,
                        sharedViewModel = sharedViewModel
                    )

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
fun AddressListComponent(
    address: List<AddressItem>,
    navigateToEditAddress: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyColumn {
        items(address) {
            AddressListItem(
                address = it,
                navigateToEditAddress = navigateToEditAddress,
                sharedViewModel = sharedViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressListTopBar(
    navigateBack: () -> Unit,
    navigateToSearchAddress: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.address_list)) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            Text(
                text = stringResource(R.string.add_address),
                modifier = Modifier.clickable { navigateToSearchAddress() },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

    )
}