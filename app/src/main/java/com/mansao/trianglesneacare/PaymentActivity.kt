package com.mansao.trianglesneacare

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mansao.trianglesneacare.utils.MidtransSDKConfig
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.UIKitCustomSetting
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.snap.TransactionResult
import com.midtrans.sdk.uikit.SdkUIFlowBuilder

class PaymentActivity : AppCompatActivity(), TransactionFinishedCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val snapToken = intent.getStringExtra("snapToken")
        initMidtransSdk()
        MidtransSDK.getInstance().startPaymentUiFlow(this, snapToken)

        Log.d("snapToken", snapToken.toString())
    }

    private fun initMidtransSdk() {
        val clientKey = MidtransSDKConfig.MERCHANT_CLIENT_KEY
        val baseUrl = MidtransSDKConfig.MERCHANT_URL
        val sdkUIFlowBuilder: SdkUIFlowBuilder = SdkUIFlowBuilder.init()
            .setClientKey(clientKey) // client_key is mandatory
            .setContext(this) // context is mandatory
            .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl(baseUrl) //set merchant url
            .enableLog(true) // enable sdk log
            .setColorTheme(
                CustomColorTheme(
                    "#FFE51255",
                    "#B61548",
                    "#FFE51255"
                )
            ) // will replace theme on snap theme on MAP
            .setLanguage("en")
        sdkUIFlowBuilder.buildSDK()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UIKitCustomSetting()
        uIKitCustomSetting.setSaveCardChecked(true)
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting)
    }

    override fun onTransactionFinished(result: TransactionResult) {
        if (result.response != null) {
            when (result.status) {
                TransactionResult.STATUS_SUCCESS -> Toast.makeText(
                    this,
                    "Transaction Finished. ID: " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()

                TransactionResult.STATUS_PENDING -> Toast.makeText(
                    this,
                    "Transaction Pending. ID: " + result.response.transactionId,
                    Toast.LENGTH_LONG
                ).show()

                TransactionResult.STATUS_FAILED -> Toast.makeText(
                    this,
                    "Transaction Failed. ID: " + result.response.transactionId.toString() + ". Message: " + result.response.statusMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (result.isTransactionCanceled) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
        } else {
            if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
            }
        }
    }
}