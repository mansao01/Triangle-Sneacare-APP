package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mansao.trianglesneacare.R

@Composable
fun ServiceNotAvailable(
    action: () -> Unit
) {
    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.server))
    AlertDialog(
        onDismissRequest = { },
        confirmButton = { action() },
        icon = {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(180.dp)
            )
        },
        text = {
            Text(text = stringResource(R.string.service_not_available))
        }
    )
}