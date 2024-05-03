package com.mansao.trianglesneacare.ui.screen.section.customer.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.CategoriesItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@Composable
fun CustomerHomeScreen(
    customerHomeViewModel: CustomerHomeViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        customerHomeViewModel.getCategories()
    }
    val username = customerHomeViewModel.username.collectAsState(initial = "").value
    customerHomeViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        val context = LocalContext.current
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> LoadingDialog()
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            is UiState.Success -> CustomerHomeContent(
                username = username,
                categoryList = uiState.data.categories
            )
        }

    }
}

@Composable
fun CustomerHomeContent(
    username: String,
    categoryList: List<CategoriesItem>
) {
    Column {
        CustomerHomeHeader(username = username)
        CategoriesList(categoryList = categoryList)
    }
}

@Composable
fun CategoriesList(
    categoryList: List<CategoriesItem>
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        )
    ) {
        items(categoryList.size) { index ->
            Text(text = categoryList[index].itemType)
        }
    }

}

@Composable
fun CustomerHomeHeader(
    username: String
) {
    Text(text = "Welcome, $username!")


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeTopBar() {
    CenterAlignedTopAppBar(title = { Text(text = "Triangle Sneacare") })
}