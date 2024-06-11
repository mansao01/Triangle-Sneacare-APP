package com.mansao.trianglesneacare.ui.screen.section.service.transaction

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.dto.ItemsItem
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CartListItemWithDropdown
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun DetailTransactionScreen(
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit,
    detailTransactionViewModel: DetailTransactionViewModel = hiltViewModel()
) {
    val transaction = sharedViewModel.transactionItem

    Scaffold(
        topBar = {
            DetailTransactionTopBar(navigateBack = navigateBack)
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .verticalScroll(rememberScrollState())
        ) {
            transaction?.items?.let {
                OrderItemListSection(
                    items = it,
                    detailTransactionViewModel = detailTransactionViewModel
                )
            }
            transaction?.let {
                PricingDetailSection(transactionItem = it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            DetailBottomSection(
                detailTransactionViewModel = detailTransactionViewModel,
                transactionId = transaction?.id ?: ""
            )

        }
    }

    UpdateWashStatusUiEvent(detailTransactionViewModel = detailTransactionViewModel)
    UpdateDeliveryStatusUiEvent(
        detailTransactionViewModel = detailTransactionViewModel,
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
fun OrderItemListSection(
    items: List<ItemsItem>,
    detailTransactionViewModel: DetailTransactionViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 160.dp, max = 320.dp)

    ) {
        items(items) { item ->
            CartListItemWithDropdown(
                image = item.imageUrl,
                serviceName = item.serviceName,
                price = item.price,
                washStatus = item.washStatus,
                orderId = item.id,
                detailTransactionViewModel = detailTransactionViewModel
            )
        }
    }
}

@Composable
fun DetailBottomSection(
    modifier: Modifier = Modifier,
    detailTransactionViewModel: DetailTransactionViewModel,
    transactionId: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {

        Button(
            onClick = {
                detailTransactionViewModel.updateDeliveryStatusById(transactionId)
            },
            modifier = modifier
        ) {
            Text(text = "Finish process")
        }
    }
}

@Composable
fun UpdateDeliveryStatusUiEvent(
    detailTransactionViewModel: DetailTransactionViewModel,
    navigateBack: () -> Unit
) {
    detailTransactionViewModel.updateDeliveryStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Log.e("Error update delivery status", uiState.errorMessage)
            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {

            }

            is UiState.Success -> navigateBack()
        }

    }
}

@Composable
fun UpdateWashStatusUiEvent(
    detailTransactionViewModel: DetailTransactionViewModel,
) {
    val context = LocalContext.current
    detailTransactionViewModel.updateWashStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Log.e("Error update delivery status", uiState.errorMessage)
            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {

            }

            is UiState.Success -> Toast.makeText(context, uiState.data.msg, Toast.LENGTH_SHORT)
                .show()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTransactionTopBar(navigateBack: () -> Unit) {
    TopAppBar(title = { Text(text = "Detail Transaction") }, navigationIcon = {
        IconButton(onClick = { navigateBack() }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
        }
    })

}
