package com.mansao.trianglesneacare.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.CategoriesItem

@Composable
fun CategoryGridItem(
    categoryItem: CategoriesItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = getCategoryIcon(categoryItem),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = categoryItem.itemType,
            )
        }
    }
}
@Composable
fun getCategoryIcon(categoryItem: CategoriesItem): Painter {
    // Return appropriate icon based on categoryItem
    return when (categoryItem.itemType) {
        "Helmet" -> painterResource(id = R.drawable.helmet)
        "Cap" -> painterResource(id = R.drawable.cap)
        "Bag" -> painterResource(id = R.drawable.bag)
        "Shoes" -> painterResource(id = R.drawable.shoes)
        else -> painterResource(id = R.drawable.helmet)
    }
}