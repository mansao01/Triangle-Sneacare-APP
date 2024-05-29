package com.mansao.trianglesneacare.ui.screen.section.customer.cart

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.CartItems
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    cartViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when(uiState){
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> CartList(cart = uiState.data.items)
        }
    }
}

@Composable
fun CartList(cart: List<CartItems>) {
    LazyColumn {
        items(cart){
            Text(text = it.service.serviceName)
        }
    }
}