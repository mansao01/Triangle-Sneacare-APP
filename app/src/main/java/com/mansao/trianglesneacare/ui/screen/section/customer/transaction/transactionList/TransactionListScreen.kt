package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.TransactionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen


@Composable
fun TransactionListScreen(transactionListViewModel: TransactionListViewModel = hiltViewModel()) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        transactionListViewModel.getTransactions()
    }
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
            UiState.Standby -> {

            }

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
    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(text = "payment status$transaction.paymentStatus")
        Text(text = "payment status${transaction.paymentMethod}")
        Text(text = transaction.totalPurchasePrice.toString())
    }
}

