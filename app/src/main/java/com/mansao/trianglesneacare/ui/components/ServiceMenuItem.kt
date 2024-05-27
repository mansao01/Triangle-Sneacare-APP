package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mansao.trianglesneacare.R
import java.text.NumberFormat
import java.util.Locale


@Composable
fun ServiceMenuItem(
    serviceName: String,
    serviceDescription: String,
    price: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val formattedPrice = NumberFormat.getNumberInstance(Locale.GERMAN).format(price.toInt())

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp), // Increased vertical padding for better spacing
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp) // Increased padding for more breathing room
        ) {
            Text(
                text = serviceName,
                style = TextStyle(
                    fontSize = 18.sp, // Increased font size for better readability
                    fontWeight = FontWeight.Bold, // Made the service name bold for emphasis
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 4.dp) // Added padding below service name for separation
            )
            Text(
                text = serviceDescription,
                style = TextStyle(
                    fontSize = 14.sp, // Slightly smaller font for description
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Use a lighter color for less emphasis
                ),
                modifier = Modifier.padding(bottom = 8.dp) // Added padding below description for separation
            )
                Text(
                    text = stringResource(R.string.rp, formattedPrice),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold, // Slightly bolder text for price
                        color = MaterialTheme.colorScheme.primary // Using primary color for price to make it stand out
                    )
                )
        }
    }
}
