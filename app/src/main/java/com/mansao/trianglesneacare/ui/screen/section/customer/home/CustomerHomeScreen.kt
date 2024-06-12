package com.mansao.trianglesneacare.ui.screen.section.customer.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.CategoriesItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CategoryGridItem
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
        val context = LocalContext.current
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> LoadingScreen()
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

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
    Column {
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