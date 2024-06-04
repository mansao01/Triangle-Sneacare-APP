package com.mansao.trianglesneacare

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PaymentActivity", "onCreate called")

        initMidtransSdk()
        Log.d("PaymentActivity", "Midtrans SDK Initialized")

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d("PaymentActivity", "Activity result received")
                if (result.resultCode == RESULT_OK) {
                    result.data?.let {
                        val transactionResult =
                            it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
                        Toast.makeText(
                            this,
                            "${transactionResult?.transactionId}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("PaymentActivity", "Transaction ID: ${transactionResult?.transactionId}")
                        //        log shown

                    }
                }
            }

        val snapToken = intent.getStringExtra("snapToken")
        Log.d("PaymentActivity", "Snap Token: $snapToken")

        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this, // Activity
            launcher, // ActivityResultLauncher
            snapToken // Snap Token
        )
        Log.d("PaymentActivity", "Payment UI flow started")
    }

    private fun initMidtransSdk() {
        val clientKey = MidtransSDKConfig.MERCHANT_CLIENT_KEY
        val baseUrl = MidtransSDKConfig.MERCHANT_URL
        UiKitApi.Builder()
            .withMerchantClientKey(clientKey) // client_key is mandatory
            .withContext(this) // context is mandatory
            .withMerchantUrl(baseUrl) // set transaction finish callback (sdk callback)
            .enableLog(true) // enable sdk log (optional)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
        Log.d("PaymentActivity", "Midtrans SDK built")
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
        Log.d("PaymentActivity", "UIKit custom settings applied")
    }

    override fun onTransactionFinished(result: TransactionResult) {
        Log.d("PaymentActivity", "Transaction finished with status: ${result.status}")
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> {
                    Log.d("PaymentActivity", "Transaction Success. ID: ${result.response.transactionId}")
                    Toast.makeText(
                        this,
                        "Transaction Finished. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TransactionResult.STATUS_PENDING -> {
                    Log.d("PaymentActivity", "Transaction Pending. ID: ${result.response.transactionId}")
                    Toast.makeText(
                        this,
                        "Transaction Pending. ID: " + result.response.transactionId,
                        Toast.LENGTH_LONG
                    ).show()
                }
                TransactionResult.STATUS_FAILED -> {
                    Log.d("PaymentActivity", "Transaction Failed. ID: ${result.response.transactionId}. Message: ${result.response.statusMessage}")
                    Toast.makeText(
                        this,
                        "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else if (result.isTransactionCanceled) {
            Log.d("PaymentActivity", "Transaction Canceled")
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Log.d("PaymentActivity", "Transaction Invalid")
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Log.d("PaymentActivity", "Transaction Finished with failure.")
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
