package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ServiceMenuItem(
    serviceName: String,
//    icon: ImageVector,
    modifier: Modifier = Modifier,
//    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
//            .clickable {
//                onClick()
//            }
            .padding(horizontal = 16.dp)
    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier
//                .size(24.dp)
//        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = serviceName,
            style = TextStyle(
                fontSize = 16.sp,
            )
        )

    }
}