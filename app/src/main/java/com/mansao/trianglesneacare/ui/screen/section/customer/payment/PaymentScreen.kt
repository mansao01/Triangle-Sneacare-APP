package com.mansao.trianglesneacare.ui.screen.section.customer.payment

import android.app.Activity
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

@Composable
fun PaymentScreen(
    sharedViewModel: SharedViewModel,
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val snapToken = sharedViewModel.snapToken
    val transactionId = sharedViewModel.transactionId
    Log.d("snap token in paymentScreen", snapToken)
    LaunchedEffect(Unit) {

        paymentViewModel.getPaymentStatus(transactionId)
    }
    PaymentUiState(
        paymentViewModel = paymentViewModel,
        sharedViewModel = sharedViewModel,
        navigateBack = navigateBack
    )
}

@Composable
fun PaymentUiState(
    paymentViewModel: PaymentViewModel,
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit
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
                    when (uiState.data.transactionStatus?: "masi kosong") {
                        "settlement" -> {
                            PaymentSuccess(navigateToTransactionList = navigateBack)
                        }

                        else -> {
                            MidtransInit(sharedViewModel = sharedViewModel)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun MidtransInit(
    sharedViewModel: SharedViewModel
) {
    val snapToken = sharedViewModel.snapToken
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("result midtrans", result.toString())
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.let {
                    val transactionResult =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            it.getParcelableExtra(
                                UiKitConstants.KEY_TRANSACTION_RESULT,
                                TransactionResult::class.java
                            )
                        } else {
                            it.getParcelableExtra(UiKitConstants.KEY_TRANSACTION_RESULT)
                        }
                    Log.d("midtrans transaction", transactionResult?.transactionId.toString())
                }
            }
        }

    Log.d("midtrans initiate", "check")
    val context = LocalContext.current
    UiKitApi.Builder()
        .withMerchantClientKey(MidtransSDKConfig.MERCHANT_CLIENT_KEY) // client_key is mandatory
        .withContext(context) // context is mandatory
        .withMerchantUrl(MidtransSDKConfig.MERCHANT_URL) // set transaction finish callback (sdk callback)
        .enableLog(true) // enable sdk log (optional)
        .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
        .build()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            val totalPrice = sharedViewModel.totalPrice
            Row {
                Text(text = "Total you should pay :")
                Text(text = totalPrice.toString())
            }
            Button(onClick = {
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    context as Activity,
                    launcher,
                    snapToken
                )
            }) {
                Text(text = "Pay")
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
