package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mansao.trianglesneacare.R

@Composable
fun LocationCheckingDialog(
    action: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.pin_location))
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { action() }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Oops, it seems your location is off", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column {
                Text(
                    text = "To use this feature, please enable location services on your device.",
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "You can enable location services by going to Settings > Location.",
                )
            }
        }
    )
}
