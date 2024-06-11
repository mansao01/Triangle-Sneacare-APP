package com.mansao.trianglesneacare.ui.screen.section.owner.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.dto.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerHomeScreen(ownerHomeViewModel: OwnerHomeViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        ownerHomeViewModel.getTransactionByMonth()

    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Welcome boss")
//        add chips to filter transactions
        AssistChip(onClick = { ownerHomeViewModel.getTransactionByMonthAndPaymentStatus()}, label = { /*TODO*/ })
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
            TransactionItem(transaction = it)
        }
    }
}

@Composable
fun TopSection(
    totalTransactionCount: Int,
    totalTransactionAmount: Int
) {
    Column {
        Text(text = totalTransactionCount.toString())
        Text(text = totalTransactionAmount.toString())
    }
}


@Composable
fun TransactionItem(transaction: TransactionsItem) {
    Text(text = transaction.user)
}