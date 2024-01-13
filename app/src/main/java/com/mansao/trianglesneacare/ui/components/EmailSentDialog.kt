package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun EmailSentDialog(
    navigateToLogin: () -> Unit,
    email: String
) {
    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.email_send))
    AlertDialog(
        onDismissRequest = { navigateToLogin() },
        confirmButton = {
            Button(onClick = { navigateToLogin() }) {
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
            Text(text = stringResource(R.string.please_verify_your_email))
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.we_sent_an_email_to, email),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.signup_notes),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
        }
    )


}