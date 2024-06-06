package com.mansao.trianglesneacare.ui.screen.section.customer.payment.checking

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun PaymentCheckingScreen(
    paymentCheckingViewModel: PaymentCheckingViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToPayment: () -> Unit,
    navigateToPaymentSuccess: () -> Unit,
    navigateToTransactionList: () -> Unit

) {

    val transactionId = sharedViewModel.transactionId
    LaunchedEffect(Unit) {
        paymentCheckingViewModel.getPaymentStatus(transactionId)
        paymentCheckingViewModel.setToStandby()

    }
    OnlinePaymentCheckUiEvent(
        transactionId = transactionId,
        paymentCheckingViewModel = paymentCheckingViewModel,
        navigateToPayment = navigateToPayment,
        navigateToTransactionList = navigateToTransactionList
    )
    UpdatePaymentUiEvent(
        paymentCheckingViewModel = paymentCheckingViewModel,
        navigateToPaymentSuccess = navigateToPaymentSuccess
    )
}

@Composable
fun OnlinePaymentCheckUiEvent(
    transactionId: String,
    paymentCheckingViewModel: PaymentCheckingViewModel,
    navigateToPayment: () -> Unit,
    navigateToTransactionList: () -> Unit

) {
    val context = LocalContext.current
    paymentCheckingViewModel.getOnlinePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> {
                Log.d("get payment status", uiState.data.transactionStatus ?: "masi kosong")
                when (uiState.data.transactionStatus) {
                    "settlement" -> {
                        LaunchedEffect(Unit) {
                            paymentCheckingViewModel.updatePaymentStatus(
                                transactionId,
                                uiState.data.transactionStatus
                            )
                        }
                    }
                    "pending" -> {
                        navigateToTransactionList()
                    }
                    else -> {
                        LaunchedEffect(Unit) {
                            navigateToPayment()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UpdatePaymentUiEvent(
    paymentCheckingViewModel: PaymentCheckingViewModel,
    navigateToPaymentSuccess: () -> Unit,
) {
    val context = LocalContext.current
    paymentCheckingViewModel.updatePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingScreen()
            UiState.Standby -> {}
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    navigateToPaymentSuccess()
                }
            }
        }
    }

}