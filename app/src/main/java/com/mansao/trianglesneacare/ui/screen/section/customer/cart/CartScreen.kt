package com.mansao.trianglesneacare.ui.screen.section.customer.cart

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.CartItems
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CartListItem
import com.mansao.trianglesneacare.ui.components.EmptyData
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    navigateToCreateTransaction: () -> Unit,
    sharedViewModel: SharedViewModel

) {
    LaunchedEffect(Unit) {
        cartViewModel.getCart()
    }
    val context = LocalContext.current
    cartViewModel.isDeleteSuccess.collectAsState(initial = false).value.let { isSuccess ->
        if (isSuccess) {
            cartViewModel.getCart()
            cartViewModel.resetDeleteSuccess()
        }
    }
    cartViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> {
                if (!cartViewModel.cartNotFound.collectAsState(initial = false).value) {
                    Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    EmptyData()
                }
            }

            UiState.Loading -> {
                LoadingScreen()
            }

            UiState.Standby -> {}
            is UiState.Success -> {
                CartContent(
                    cart = uiState.data.items,
                    totalPrice = uiState.data.totalPrice.toString(),
                    cartViewModel = cartViewModel,
                    navigateToCreateTransaction = navigateToCreateTransaction,
                    sharedViewModel = sharedViewModel
                )

            }
        }
    }
}

@Composable
fun CartContent(
    modifier: Modifier = Modifier,
    cart: List<CartItems>,
    totalPrice: String,
    cartViewModel: CartViewModel,
    navigateToCreateTransaction: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    if (cart.isEmpty()) {
        EmptyData()
    } else {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            val formattedPrice =
                NumberFormat.getNumberInstance(Locale.GERMAN).format(totalPrice.toInt())

            Box(modifier = Modifier.weight(0.9f)) {
                CartList(cart = cart, cartViewModel = cartViewModel)
            }
            Row(
                modifier.padding(top = 22.dp, bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(start = 16.dp)) {

                    Text(
                        text = "Total in cart",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                    )
                    Text(
                        text = stringResource(R.string.rp, formattedPrice),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Box(modifier = Modifier.weight(0.1f)) {
                    if (totalPrice != 0.toString()) {
                        Button(
                            onClick = {
                                navigateToCreateTransaction()
                                sharedViewModel.addTotalPrice(totalPrice.toInt())
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.checkout))
                        }
                    }
                }
            }

        }
    }

}

@Composable
fun CartList(cart: List<CartItems>, cartViewModel: CartViewModel) {
    if (cart.isNotEmpty()) {
        LazyColumn {

            items(cart) {
                CartListItem(
                    image = it.imageUrl,
                    serviceName = it.service.serviceName,
                    price = it.service.price,
                    onDeleteClick = {
                        cartViewModel.deleteOrder(it.id)
                    }
                )
            }
        }
    } else {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "Empty Here")
        }

    }
}

