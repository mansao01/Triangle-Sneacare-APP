package com.mansao.trianglesneacare

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_FAILED
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_INVALID
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_PENDING
import com.midtrans.sdk.corekit.models.snap.TransactionResult.STATUS_SUCCESS
import com.midtrans.sdk.uikit.api.model.CustomColorTheme
import com.midtrans.sdk.uikit.api.model.TransactionResult
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.midtrans.sdk.uikit.internal.util.UiKitConstants.STATUS_CANCELED

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val snapToken = intent.getStringExtra("snapToken")
        Log.d("snapToken", snapToken ?:"null snapToken")

        initMidtransSdk()

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
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
                        if (transactionResult != null) {
                            Log.d("transaction status:", transactionResult.status)
                            Log.d("transaction result:", transactionResult.transactionId.toString())
                            Log.d("transaction result:", transactionResult.message.toString())
                            when (transactionResult.status) {
                                STATUS_SUCCESS -> {
                                    Toast.makeText(
                                        this,
                                        "Transaction Finished. ID: " + transactionResult.transactionId,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                STATUS_PENDING -> {
                                    Toast.makeText(
                                        this,
                                        "Transaction Pending. ID: " + transactionResult.transactionId,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                STATUS_FAILED -> {
                                    Toast.makeText(
                                        this,
                                        "Transaction Failed. ID: " + transactionResult.transactionId,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                STATUS_CANCELED -> {
                                    Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG)
                                        .show()
                                }

                                STATUS_INVALID -> {
                                    Toast.makeText(
                                        this,
                                        "Transaction Invalid. ID: " + transactionResult.transactionId,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                else -> {
                                    Toast.makeText(
                                        this,
                                        "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

        Log.d("PaymentActivity", "Snap Token: $snapToken")

        UiKitApi.getDefaultInstance().startPaymentUiFlow(
            this,
            launcher,
            snapToken
        )
    }

    private fun initMidtransSdk() {
        val clientKey = MidtransSDKConfig.MERCHANT_CLIENT_KEY
        val baseUrl = MidtransSDKConfig.MERCHANT_URL
        UiKitApi.Builder()
            .withMerchantClientKey(clientKey) // client_key is mandatory
            .withContext(this) // context is mandatory
            .withMerchantUrl(baseUrl)
            .enableLog(true) // enable sdk log (optional)
            .withColorTheme(CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
            .build()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }
}

//    fun openActivityForResult() {
//        startForResult.launch(Intent(this, PaymentActivity::class.java))
//    }
//
//    private val startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val intent = result.data
//                // Handle the Intent
//                //do stuff here
//            }
//        }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == RESULT_OK) {
//            val transactionResult =
//                data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
//                    UiKitConstants.KEY_TRANSACTION_RESULT
//                )
//            if (transactionResult != null) {
//                when (transactionResult.status) {
//                    STATUS_SUCCESS -> {
//                        Toast.makeText(
//                            this,
//                            "Transaction Finished. ID: " + transactionResult.transactionId,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                    STATUS_PENDING -> {
//                        Toast.makeText(
//                            this,
//                            "Transaction Pending. ID: " + transactionResult.transactionId,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                    STATUS_FAILED -> {
//                        Toast.makeText(
//                            this,
//                            "Transaction Failed. ID: " + transactionResult.transactionId,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                    STATUS_CANCELED -> {
//                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
//                    }
//
//                    STATUS_INVALID -> {
//                        Toast.makeText(
//                            this,
//                            "Transaction Invalid. ID: " + transactionResult.transactionId,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                    else -> {
//                        Toast.makeText(
//                            this,
//                            "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status,
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//}
