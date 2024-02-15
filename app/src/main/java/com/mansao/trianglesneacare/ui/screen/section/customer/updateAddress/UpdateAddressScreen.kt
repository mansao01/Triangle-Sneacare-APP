package com.mansao.trianglesneacare.ui.screen.section.customer.updateAddress

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.dto.Address
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@Composable
fun UpdateAddressScreen(
    updateAddressViewModel: UpdateAddressViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    updateAddressViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Standby -> {}
            is UiState.Loading -> LoadingDialog()
            is UiState.Success -> UpdateAddressComponent(address = uiState.data.address)
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

}

@Composable
fun UpdateAddressComponent(
    address: Address
) {

}