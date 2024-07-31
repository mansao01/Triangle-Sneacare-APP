package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Dry
import androidx.compose.material.icons.filled.Wash
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.screen.section.service.transaction.DetailTransactionViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartListItem(
    image: String,
    serviceName: String,
    price: Int,
    onDeleteClick: () -> Unit,
) {
    val context = LocalContext.current
    val formattedPrice =
        NumberFormat.getNumberInstance(Locale.GERMAN).format(price)
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(93.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(image)
                        .build(),
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(4.dp)
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = serviceName)
                Text(text = formattedPrice)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

@Composable
fun CartListItemSimple(
    image: String,
    serviceName: String,
    price: Int,
) {
    val context = LocalContext.current
    val formattedPrice =
        NumberFormat.getNumberInstance(Locale.GERMAN).format(price)
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(image)
                        .build(),
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = serviceName)
                Text(text = formattedPrice)
            }
        }
    }
}

@Composable
fun CartListItemWithDropdown(
    image: String,
    serviceName: String,
    price: Int,
    washStatus: String,
    detailTransactionViewModel: DetailTransactionViewModel,
    orderId: String
) {
    val context = LocalContext.current
    val formattedPrice = NumberFormat.getNumberInstance(Locale.GERMAN).format(price)

    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(image)
                        .build(),
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    },
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = serviceName)
                Text(text = formattedPrice)
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = "Wash status")
            WashStatusDropDown(
                washStatus = washStatus,
                detailTransactionViewModel = detailTransactionViewModel,
                orderId = orderId
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WashStatusDropDown(
    washStatus: String,
    detailTransactionViewModel: DetailTransactionViewModel,
    orderId: String
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var selectedWashStatus by remember { mutableStateOf(washStatus) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier
            .padding(16.dp)
    ) {
        TextField(
            value = selectedWashStatus,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Row {
                        Icon(imageVector = Icons.Default.Wash, contentDescription = null)
                        Text(stringResource(R.string.washing))

                    }
                },
                onClick = {
                    isExpanded = false
                    selectedWashStatus = context.getString(R.string.washing)
                    detailTransactionViewModel.updateWashStatus(orderId, "washing")
                },
            )

            DropdownMenuItem(
                text = {
                    Row {
                        Icon(imageVector = Icons.Default.Dry, contentDescription = null)

                        Text(stringResource(R.string.drying))

                    }
                },
                onClick = {
                    isExpanded = false
                    selectedWashStatus = context.getString(R.string.drying)
                    detailTransactionViewModel.updateWashStatus(orderId, "drying")
                },
            )

            DropdownMenuItem(
                text = {
                    Row {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)

                        Text(stringResource(R.string.finished))
                    }
                },
                onClick = {
                    isExpanded = false
                    selectedWashStatus = context.getString(R.string.finished)
                    detailTransactionViewModel.updateWashStatus(orderId, "finish")
                },
            )
        }
    }
}
