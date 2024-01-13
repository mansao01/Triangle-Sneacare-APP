package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mansao.trianglesneacare.R

@Composable
fun ForbiddenScreen(openDialog: Boolean, modifier: Modifier = Modifier) {
    val dialogState = remember { mutableStateOf(openDialog) }
    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.no_acceess))
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {
                Text(
                    text = stringResource(R.string.confirm),
                    modifier = modifier.clickable { dialogState.value = !dialogState.value }
                )
            },
            icon = {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(120.dp)
                )
            },
            text = {
                Text(text = stringResource(R.string.email_not_verified))
            }
        )
    }
}