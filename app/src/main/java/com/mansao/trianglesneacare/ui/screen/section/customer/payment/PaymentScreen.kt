package com.mansao.trianglesneacare.ui.screen.section.customer.payment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

@Composable
fun PaymentScreen(
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit
) {
    val snapToken = sharedViewModel.snapToken
    Log.d("snap token in paymentScreen", snapToken)
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
                navigateBack()
            }
        }
    MidtransInit(snapToken = snapToken, launcher = launcher)
}


@Composable
fun MidtransInit(
    snapToken: String,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {

    val context = LocalContext.current
    UiKitApi.Builder()
        .withMerchantClientKey(MidtransSDKConfig.MERCHANT_CLIENT_KEY) // client_key is mandatory
        .withContext(context) // context is mandatory
        .withMerchantUrl(MidtransSDKConfig.MERCHANT_URL) // set transaction finish callback (sdk callback)
        .enableLog(true) // enable sdk log (optional)
        .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
        .build()

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
