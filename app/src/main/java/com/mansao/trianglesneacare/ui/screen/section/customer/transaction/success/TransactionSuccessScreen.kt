package com.mansao.trianglesneacare.ui.screen.section.customer.transaction.success

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mansao.trianglesneacare.R

@Composable
fun TransactionSuccessScreen(
    navigateToTransactionList: () -> Unit
) {
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
            text = "Transaction Success",
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
