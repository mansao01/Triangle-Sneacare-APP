package com.mansao.trianglesneacare.ui.screen.section.customer.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun PaymentScreen(
    sharedViewModel: SharedViewModel,
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToMidtrans: () -> Unit
) {
    val transactionId = sharedViewModel.transactionId
    LaunchedEffect(Unit) {

        paymentViewModel.getPaymentStatus(transactionId)
    }
    PaymentUiState(
        paymentViewModel = paymentViewModel,
        navigateBack = navigateBack,
        navigateToMidtrans = navigateToMidtrans
    )
}

@Composable
fun PaymentUiState(
    paymentViewModel: PaymentViewModel,
    navigateBack: () -> Unit,
    navigateToMidtrans: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        paymentViewModel.getOnlinePaymentStatusUiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
            when (uiState) {
                is UiState.Error -> Toast.makeText(
                    context,
                    uiState.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()

                UiState.Loading -> LoadingScreen()
                UiState.Standby -> {}
                is UiState.Success -> {
                    when (uiState.data.transactionStatus ?: "masi kosong") {
                        "settlement" -> {
                            PaymentSuccess(navigateToTransactionList = navigateBack)
                            Log.d("payment midtrans", uiState.data.transactionStatus?:"null")
                        }
                        else -> {
                            LaunchedEffect(Unit) {

                                navigateToMidtrans()
                            }
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun PaymentSuccess(
    navigateToTransactionList: () -> Unit
) {
    Log.d("payment success", "check")

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.success_animation))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie animation for success
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .size(150.dp) // Increased size for better visibility
                .padding(bottom = 24.dp) // Added padding for spacing
        )

        // Success message text with manual styling
        Text(
            text = "Payment Success",
            style = TextStyle(
                fontSize = 24.sp, // Set the font size to 24sp
                fontWeight = FontWeight.Bold, // Make the text bold
            ),
            modifier = Modifier.padding(bottom = 16.dp) // Padding for spacing
        )

        // Finish button to navigate back
        Button(
            onClick = navigateToTransactionList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Finish")
        }
    }
}
