package com.mansao.trianglesneacare.ui.screen.section.owner.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.section.service.home.OrderList
import java.text.NumberFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerHomeScreen(ownerHomeViewModel: OwnerHomeViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(32.dp))
        HeaderText(
            text = "Welcome boss",
            description = "Here is some information for you",
            showDescription = true,
            modifier = Modifier.padding(start = 16.dp)
        )

        ChipsSection(ownerHomeViewModel = ownerHomeViewModel)

        OwnerHome(ownerHomeViewModel = ownerHomeViewModel)
    }
}

@Composable
fun OwnerHome(ownerHomeViewModel: OwnerHomeViewModel) {
    val context = LocalContext.current
    ownerHomeViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> HomeContent(transactions = uiState.data.transactions)
        }

    }
}

@Composable
fun HomeContent(transactions: List<TransactionsItem>) {
    val totalTransactionAmount = transactions.sumOf { it.totalPurchasePrice }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            TopSection(
                totalTransactionCount = transactions.size,
                totalTransactionAmount = totalTransactionAmount
            )
        }
        items(transactions) {
            TransactionListItem(transaction = it, clickAction = {})
        }
    }
}

@Composable
fun TopSection(
    totalTransactionCount: Int,
    totalTransactionAmount: Int
) {
    val formattedPrice =
        NumberFormat.getNumberInstance(Locale.GERMAN).format(totalTransactionAmount)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Transactions",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = totalTransactionCount.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total amount",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.rp, formattedPrice),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChipsSection(ownerHomeViewModel: OwnerHomeViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = { ownerHomeViewModel.getAllTransaction() },
            label = { Text(text = "All Transaction") }
        )
        AssistChip(
            onClick = { ownerHomeViewModel.getTransactionByMonth() },
            label = { Text(text = "All This Month") }
        )
        AssistChip(
            onClick = { ownerHomeViewModel.getTransactionByMonthAndPaymentStatus("settlement") },
            label = { Text(text = "This Month Success") }
        )
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