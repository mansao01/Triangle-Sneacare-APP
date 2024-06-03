package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.success

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TransactionSuccessScreen(
    modifier: Modifier = Modifier, navigateToTransactionList: () -> Unit
) {
    Text(text = "Transaction Success")

    Button(onClick = { navigateToTransactionList()}) {
        Text(text = "Click me!")
    }
}