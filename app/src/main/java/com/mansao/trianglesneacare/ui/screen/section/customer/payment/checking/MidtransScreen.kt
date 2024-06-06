package com.mansao.trianglesneacare.ui.screen.section.customer.payment.checking

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants


@Composable
fun MidtransScreen(sharedViewModel: SharedViewModel, navigateBack: () -> Unit) {
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
                    navigateBack()
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val totalPrice = sharedViewModel.totalPrice
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // This arranges children with space between
                verticalAlignment = Alignment.CenterVertically // Aligns children vertically centered
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total you should pay: ",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 4.dp) // Padding between texts
                    )
                    Text(
                        text = totalPrice.toString(),
                        fontSize = 16.sp
                    )
                }
                Button(
                    onClick = {
                        UiKitApi.getDefaultInstance().startPaymentUiFlow(
                            context as Activity,
                            launcher,
                            snapToken
                        )
                    }
                ) {
                    Text(text = "Pay")
                }
            }
        }
    }
}