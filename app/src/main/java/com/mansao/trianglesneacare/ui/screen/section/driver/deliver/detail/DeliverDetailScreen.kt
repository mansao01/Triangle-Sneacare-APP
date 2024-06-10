package com.mansao.trianglesneacare.ui.screen.section.driver.deliver.detail

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.mansao.trianglesneacare.data.network.response.dto.ItemsItem
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CartListItemSimple
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.utils.MapsNavigateUtil

@Composable
fun DeliverDetailScreen(
    sharedViewModel: SharedViewModel,
    deliverDetailViewModel: DeliverDetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val transactionItem = sharedViewModel.transactionItem

    val latitude = transactionItem?.customerAddress?.latitude ?: 0.0
    val longitude = transactionItem?.customerAddress?.longitude ?: 0.0

    val paymentMethod = transactionItem?.paymentMethod ?: ""
    val transactionId = transactionItem?.id ?: ""

    Scaffold(
        topBar = { DeliverTopBar { navigateBack() } }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Order Items",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )

                transactionItem?.items?.let {
                    OrderItemListSection(
                        items = it
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pricing Details",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )

                transactionItem?.let {
                    PricingDetailSection(
                        transactionItem = it
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

            }

            DeliverDetailBottomSection(
                latitude = latitude,
                longitude = longitude,
                context = context,
                deliverDetailViewModel = deliverDetailViewModel,
                transactionId = transactionItem?.id ?: "",

                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
    UpdateDeliveryStatusUiEvent(
        deliverDetailViewModel = deliverDetailViewModel,
        navigateBack = navigateBack,
        paymentMethod = paymentMethod,
        transactionId = transactionId
    )

    UpdatePaymentStatusUiEvent(
        deliverDetailViewModel = deliverDetailViewModel,
        navigateBack = navigateBack
    )
}


@Composable
fun PricingDetailSection(modifier: Modifier = Modifier, transactionItem: TransactionsItem) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DetailRow(label = "Delivery Method:", value = transactionItem.deliveryMethod)
            DetailRow(label = "Payment Method:", value = transactionItem.paymentMethod)
            DetailRow(label = "Payment Status:", value = transactionItem.paymentStatus)
            DetailRow(
                label = "Total Purchase Price:",
                value = "${transactionItem.totalPurchasePrice}"
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun OrderItemListSection(items: List<ItemsItem>) {
    LazyColumn(
        modifier = Modifier.height(120.dp)
    ) {
        items(items) { item ->
            CartListItemSimple(
                image = item.imageUrl,
                serviceName = item.serviceName,
                price = item.price
            )
        }
    }

}

@Composable
fun DeliverDetailBottomSection(
    modifier: Modifier = Modifier,
    latitude: Double,
    longitude: Double,
    context: Context,
    deliverDetailViewModel: DeliverDetailViewModel,
    transactionId: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Button(
            onClick = {
                MapsNavigateUtil.pinLocationMap(latitude.toString(), longitude.toString(), context)
            }, modifier = modifier
        ) {
            Text(text = stringResource(R.string.navigate))
        }

        Button(
            onClick = {
                deliverDetailViewModel.updateDeliveryStatusById(transactionId)
            },
            modifier = modifier
        ) {
            Text(text = "Finish")
        }
    }
}

@Composable
fun UpdateDeliveryStatusUiEvent(
    deliverDetailViewModel: DeliverDetailViewModel,
    navigateBack: () -> Unit,
    paymentMethod: String,
    transactionId: String
) {
    deliverDetailViewModel.updateDeliveryStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Log.e("Error update delivery status", uiState.errorMessage)
            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {

            }

            is UiState.Success -> {
                if (paymentMethod == "Cash on delivery(COD)") {
                    deliverDetailViewModel.updatePaymentStatus(transactionId)
                } else {
                    navigateBack()
                }
            }
        }

    }
}


@Composable
fun UpdatePaymentStatusUiEvent(
    deliverDetailViewModel: DeliverDetailViewModel,
    navigateBack: () -> Unit,
) {
    deliverDetailViewModel.updatePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Log.e("Error update payment status", uiState.errorMessage)
            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {

            }

            is UiState.Success -> {
                navigateBack()
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliverTopBar(navigateBack: () -> Unit) {
    TopAppBar(title = { Text(text = "Pick up details") }, navigationIcon = {
        IconButton(onClick = { navigateBack() }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
        }
    })

}
