package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.AddressItem

@Composable
fun AddressListItem(
    address: AddressItem,
    navigateToEditAddress: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            address.title?.let { Text(text = it, fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.height(4.dp))
            address.receiverName?.let { Text(text = it, fontWeight = FontWeight.Bold) }
            address.fullAddress?.let { Text(text = it, fontWeight = FontWeight.Thin) }
            address.notes?.let { Text(text = it, fontWeight = FontWeight.Thin) }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navigateToEditAddress() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.edit))
            }
            Spacer(modifier = Modifier.height(8.dp))

        }
    }

}