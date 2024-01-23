package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mansao.trianglesneacare.data.network.response.DriversItem

@Composable
fun DriverItem(
    driversItem: DriversItem,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(93.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(driversItem.pictureUrl)
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
            Column {
                Text(
                    text = driversItem.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = driversItem.email,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )

                Text(
                    text = driversItem.phone,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

