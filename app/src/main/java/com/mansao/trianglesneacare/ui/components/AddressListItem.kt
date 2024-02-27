package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.AddressItem
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.ui.theme.Inter
import com.mansao.trianglesneacare.ui.theme.Roboto
import com.mansao.trianglesneacare.ui.theme.Rubik

@Composable
fun AddressListItem(
    address: AddressItem,
    navigateToEditAddress: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = address.title ?: "",
                style = TextStyle(
                    fontFamily = Roboto,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = address.receiverName ?: "",
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = address.fullAddress ?: "",
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                ),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = address.notes ?: "",

                style = TextStyle(
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    navigateToEditAddress()
                    sharedViewModel.addAddressId(address.id)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.edit))
            }
        }
    }
}
