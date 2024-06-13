package com.mansao.trianglesneacare.ui.screen.section.service.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.dto.ItemsItem
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel


@Composable
fun ServiceHomeScreen(
    serviceHomeViewModel: ServiceHomeViewModel = hiltViewModel(),
    navigateToDetail: () -> Unit,
    navigateToAddOrder: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(Unit) {
        serviceHomeViewModel.getTransactionsByDeliveryStatus("already picked up")
    }
    ServiceHome(
        serviceHomeViewModel = serviceHomeViewModel,
        navigateToDetail = navigateToDetail,
        sharedViewModel = sharedViewModel,
        navigateToAddOrder = navigateToAddOrder
    )
}


@Composable
fun ServiceHome(
    modifier: Modifier = Modifier,
    serviceHomeViewModel: ServiceHomeViewModel,
    navigateToDetail: () -> Unit,
    navigateToAddOrder: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    Scaffold(
        topBar = { ServiceHomeTopBar() },
    ) { scaffoldPadding ->
        Surface(modifier.padding(scaffoldPadding)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ChipsSection(serviceHomeViewModel = serviceHomeViewModel)
                ServiceHomeContent(
                    serviceHomeViewModel = serviceHomeViewModel,
                    navigateToDetail = navigateToDetail,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}

@Composable
fun ChipsSection(serviceHomeViewModel: ServiceHomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = { serviceHomeViewModel.getTransactionsByDeliveryStatus("already picked up") },
            label = { Text(text = "Already picked up") }
        )
        AssistChip(
            onClick = { serviceHomeViewModel.getTransactionsByDeliveryStatus("ready to pick up") },
            label = { Text(text = "Ready to pick up") }
        )
        AssistChip(
            onClick = { serviceHomeViewModel.getTransactionsByDeliveryStatus("ready to deliver") },
            label = { Text(text = "Ready to deliver") }
        )
        AssistChip(
            onClick = { serviceHomeViewModel.getTransactionsByDeliveryStatus("already delivered to customer") },
            label = { Text(text = "Already delivered to customer") }
        )
    }
}

@Composable
fun ServiceHomeContent(
    serviceHomeViewModel: ServiceHomeViewModel,
    navigateToDetail: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    serviceHomeViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_SHORT
            ).show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> TransactionListSectionComponent(
                transaction = uiState.data.transactions,
                navigateToDetail = navigateToDetail,
                sharedViewModel = sharedViewModel
            )
        }
    }

}

@Composable
fun TransactionListSectionComponent(
    transaction: List<TransactionsItem>,
    navigateToDetail: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(transaction) {
            TransactionListItem(
                transaction = it,
                clickAction = {
                    navigateToDetail()
                    sharedViewModel.addTransactionItem(it)
                }
            )
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: TransactionsItem,
    clickAction: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { clickAction() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = transaction.user,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OrderList(orders = transaction.items)
        }
    }
}

@Composable
fun OrderList(orders: List<ItemsItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        orders.forEach { order ->
            OrderListItem(order = order)
            Divider(modifier = Modifier.padding(vertical = 8.dp))  // Divider between orders
        }
    }
}

@Composable
fun OrderListItem(order: ItemsItem) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)  // Padding above and below the row
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(context)
                .crossfade(true)
                .data(order.imageUrl)
                .build(),
            contentDescription = null,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                )
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)  // Fixed size for the image
                .padding(end = 8.dp)  // Padding to the right of the image
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = order.serviceName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                modifier = Modifier.padding(bottom = 4.dp)  // Padding below the service name
            )
            OrderListItemContentRow(key = "Wash status", value = order.washStatus)
            OrderListItemContentRow(key = "Price", value = order.price.toString())
        }
    }
}

@Composable
fun OrderListItemContentRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)  // Padding above and below the row
    ) {
        Text(
            text = key,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.weight(1f)  // Weight to divide space evenly
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.weight(1f)  // Weight to divide space evenly
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHomeTopBar(
) {
    LargeTopAppBar(title = {
        HeaderText(
            text = stringResource(R.string.orders),
            description = "",
            showDescription = false
        )
    })
}