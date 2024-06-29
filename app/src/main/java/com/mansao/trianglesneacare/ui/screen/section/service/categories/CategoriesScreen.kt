package com.mansao.trianglesneacare.ui.screen.section.service.categories

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.CategoriesItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.CategoryMenuItem
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    navigateToServiceList: () -> Unit,
    navigateToAddCategory: () -> Unit,
    sharedViewModel: SharedViewModel

) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getCategories()

    }
    val context = LocalContext.current
    val role = viewModel.role.collectAsState(initial = "").value
    viewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {
            }

            is UiState.Success -> {
                CategoriesContent(
                    categories = uiState.data.categories,
                    navigateToServiceList = navigateToServiceList,
                    navigateToAddCategory = navigateToAddCategory,
                    sharedViewModel =  sharedViewModel,
                    role = role
                )
            }
        }

    }
}

@Composable
fun CategoriesContent(
    categories: List<CategoriesItem>,
    navigateToServiceList: () -> Unit,
    navigateToAddCategory: () -> Unit,
    sharedViewModel: SharedViewModel,
    role: String
) {
    Scaffold(
        topBar = { CategoriesTopBar(navigateToAddCategory = navigateToAddCategory, role = role) }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            LazyColumn {
                items(categories) {
                    CategoryMenuItem(
                        categoryName = it.itemType,
                        onClick = {
                            sharedViewModel.addCategoryId(it.id)
                            navigateToServiceList()
                        })
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesTopBar(
    navigateToAddCategory: () -> Unit,
    role:String
) {
    LargeTopAppBar(
        title = {
            HeaderText(
                text = "Categories",
                description = "",
                showDescription = false
            )
        },

        actions = {
            if (role == "service")
            IconButton(onClick = { navigateToAddCategory() }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)

            }
        }
    )
}