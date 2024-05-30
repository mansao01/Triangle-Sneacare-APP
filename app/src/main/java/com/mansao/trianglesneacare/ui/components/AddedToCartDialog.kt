package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
fun AddedToCartDialog(
    onClick: () -> Unit,
) {
    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.added_to_cart))
    AlertDialog(
        onDismissRequest = { onClick() },
        confirmButton = {
            Button(onClick = { onClick() }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        icon = {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(120.dp)
            )
        },
        title = {
            Text(text = stringResource(R.string.item_added_to_cart))
        }
    )
}