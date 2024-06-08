package com.mansao.trianglesneacare.ui.screen.section.owner.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
}

@Composable
fun OwnerHomeContent(modifier: Modifier = Modifier, ownerHomeViewModel: OwnerHomeViewModel) {
    val context = LocalContext.current
    ownerHomeViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> ThisMonthTransactionList(transactions = uiState.data.transactions)
        }

    }
}

@Composable
fun ThisMonthTransactionList(modifier: Modifier = Modifier, transactions: List<TransactionsItem>) {

}