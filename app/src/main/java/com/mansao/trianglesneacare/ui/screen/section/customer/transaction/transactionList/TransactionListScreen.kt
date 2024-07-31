package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeliveryDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.mansao.trianglesneacare.ui.components.EmptyData
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import java.text.NumberFormat
import java.util.Locale


@Composable
fun TransactionListScreen(transactionListViewModel: TransactionListViewModel = hiltViewModel()) {
    val context = LocalContext.current
    transactionListViewModel.getTransactions()
    TransactionListContent(transactionListViewModel = transactionListViewModel, context = context)
}

@Composable
fun TransactionListContent(
    transactionListViewModel: TransactionListViewModel,
    context: Context
) {
    transactionListViewModel.transactionsUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingScreen()
            UiState.Standby -> {}
            is UiState.Success -> {
                TransactionListSectionComponent(
                    transaction = uiState.data.transactions,
                )
            }
        }
    }
}

@Composable
fun TransactionListSectionComponent(
    transaction: List<TransactionsItem>,
) {
    if (transaction.isEmpty()) EmptyData()
    else
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {  // Adding padding to the LazyColumn

            items(transaction) {
                TransactionListItem(
                    transaction = it,
                )
            }
        }
}

@Composable
fun TransactionListItem(
    transaction: TransactionsItem,
) {
    val price = transaction.totalPurchasePrice
    val formattedPrice =
        NumberFormat.getNumberInstance(Locale.GERMAN).format(price)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .padding(16.dp)  // Adding padding inside the column
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Outlined.DeliveryDining, contentDescription = null)
                Text(text = transaction.deliveryStatus)
            }

            OrderList(orders = transaction.items)
            Text(
                text = "Total Purchase Price: ",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding(bottom = 8.dp)  // Padding below the title
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.money_stack_svgrepo_com),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(id = R.string.rp, formattedPrice))
            }
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
                .size(60.dp)  // Fixed size for the image
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