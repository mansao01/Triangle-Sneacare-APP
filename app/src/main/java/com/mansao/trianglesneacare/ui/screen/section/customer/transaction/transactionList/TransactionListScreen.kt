package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.transactionList

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
    UpdatePaymentStatus(transactionListViewModel = transactionListViewModel)

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
                    transactionListViewModel = transactionListViewModel,
                    context = context
                )
            }
        }
    }
}

@Composable
fun TransactionListSectionComponent(
    transaction: List<TransactionsItem>,
    transactionListViewModel: TransactionListViewModel,
    context: Context
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(transaction) {
            TransactionListItem(
                transaction = it,
                context = context,
                transactionListViewModel = transactionListViewModel
            )
            if (it.paymentMethod == "Online Payment") {
                transactionListViewModel.getPaymentStatus(it.id)
            }
        }
    }


}

@Composable
fun TransactionListItem(
    transaction: TransactionsItem,
    context: Context,
    transactionListViewModel: TransactionListViewModel
) {
    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        val onlinePaymentStatusUpdated = transactionListViewModel.paymentStatus.collectAsState(initial = "").value
        if (transaction.paymentMethod == "Online Payment") {
            PaymentStatusCheck(transactionListViewModel = transactionListViewModel, context = context)
            transactionListViewModel.updatePaymentStatus(transaction.id, paymentStatus =onlinePaymentStatusUpdated)
        } else {
            Text(text = transaction.paymentStatus)
        }
        Text(text = transaction.paymentMethod)
        Text(text = transaction.totalPurchasePrice.toString())
    }
}

@Composable
fun PaymentStatusCheck(
    transactionListViewModel: TransactionListViewModel,
    context: Context,
) {
    transactionListViewModel.getOnlinePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                )
            }

            UiState.Standby -> {

            }

            is UiState.Success -> {
                val transactionStatus = uiState.data.transactionStatus ?: "Unknown"
                Text(text = "Payment Status: $transactionStatus")
                Log.d("Payment Status:", transactionStatus)
                transactionListViewModel.setPaymentStatus(transactionStatus)
            }
        }
    }
}



@Composable
fun UpdatePaymentStatus(
    transactionListViewModel: TransactionListViewModel
) {
    transactionListViewModel.updatePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Log.e("Update payment error:", uiState.errorMessage)
            UiState.Loading -> LoadingScreen()
            UiState.Standby -> {}
            is UiState.Success -> Log.d("Update payment success:", uiState.data.msg)
        }
    }
}

