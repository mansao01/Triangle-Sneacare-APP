package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mansao.trianglesneacare.data.network.response.PredictionsItem

@Composable
fun AutoCompleteSearchAddressListItem(
    address: PredictionsItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.align(Alignment.Top)
        )
        Column {
            address.structuredFormatting?.mainText?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold
                )
            }
            address.structuredFormatting?.secondaryText?.let { Text(text = it) }
        }
    }
}