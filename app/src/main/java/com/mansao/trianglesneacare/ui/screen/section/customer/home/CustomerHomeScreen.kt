package com.mansao.trianglesneacare.ui.screen.section.customer.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.CategoriesItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CategoryGridItem
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun CustomerHomeScreen(
    customerHomeViewModel: CustomerHomeViewModel = hiltViewModel(),
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel
) {

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
                categoryList = uiState.data.categories,
                navigateToServiceSelection = navigateToServiceSelection,
                sharedViewModel = sharedViewModel
            )
        }

    }
}

@Composable
fun CustomerHomeContent(
    username: String,
    categoryList: List<CategoriesItem>,
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        CustomerHomeHeader(username = username, modifier = Modifier.padding(start = 16.dp))
        CategoriesGrid(
            categoryList = categoryList,
            navigateToServiceSelection = navigateToServiceSelection,
            sharedViewModel = sharedViewModel
        )
    }
}

@Composable
fun CategoriesGrid(
    categoryList: List<CategoriesItem>,
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(categoryList.size) { index ->
            CategoryGridItem(
                categoryItem = categoryList[index],
                onClick = {
                    navigateToServiceSelection()
                    sharedViewModel.addCategoryName(categoryList[index].itemType)
                    sharedViewModel.addCategoryId(categoryList[index].id)
                }
            )
        }
    }

}

@Composable
fun CustomerHomeHeader(
    username: String,
    modifier: Modifier = Modifier
) {
    Text(text = "Welcome, $username!", modifier = modifier)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeTopBar() {
    CenterAlignedTopAppBar(title = { Text(text = "Triangle Sneacare") })
}