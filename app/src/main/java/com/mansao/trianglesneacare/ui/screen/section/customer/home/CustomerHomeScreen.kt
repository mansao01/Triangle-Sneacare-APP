package com.mansao.trianglesneacare.ui.screen.section.customer.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.CategoriesItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CategoryGridItem
import com.mansao.trianglesneacare.ui.components.ErrorScreen
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun CustomerHomeScreen(
    customerHomeViewModel: CustomerHomeViewModel = hiltViewModel(),
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val username = customerHomeViewModel.username.collectAsState(initial = "").value

    CustomerHomeContent(
        customerHomeViewModel = customerHomeViewModel,
        navigateToServiceSelection = { navigateToServiceSelection() },
        sharedViewModel = sharedViewModel,
        username = username
    )

}

@Composable
fun CustomerHomeContent(
    customerHomeViewModel: CustomerHomeViewModel,
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel,
    username: String
) {

    customerHomeViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> LoadingScreen()
            is UiState.Error -> {
                ErrorScreen {
                    customerHomeViewModel.getCategories()
                }
            }

            is UiState.Success -> CustomerHome(
                categoryList = uiState.data.categories,
                navigateToServiceSelection = { navigateToServiceSelection() },
                sharedViewModel = sharedViewModel,
                username = username
            )
        }

    }
}

@Composable
fun CustomerHome(
    categoryList: List<CategoriesItem>,
    navigateToServiceSelection: () -> Unit,
    sharedViewModel: SharedViewModel,
    username: String
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        CustomerHomeHeader(username = username)
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
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(4.dp))
            Icon(
                painter = painterResource(
                    id = R.drawable.default_logo
                ),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Text(
                text = "Welcome, $username!",
                style = TextStyle(fontSize = 22.sp),
                modifier = Modifier.padding(start = 16.dp)
            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Triangle Sneacare  can help you to clean your stuff",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomerHeaderPreview() {
    CustomerHomeHeader(username = "Mansao")
    
}