package com.mansao.trianglesneacare.ui.screen.section.customer.serviceSelection

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.response.ServiceItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen
import com.mansao.trianglesneacare.ui.components.ServiceMenuSelectionItem
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun ServiceSelectionScreen(
    sharedViewModel: SharedViewModel,
    serviceSelectionViewModel: ServiceSelectionViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToUploadImage: () -> Unit,
) {
    val categoryId = sharedViewModel.categoryId
    val categoryName = sharedViewModel.categoryName

    LaunchedEffect(Unit) {
        serviceSelectionViewModel.getServicesByCategoryId(categoryId)
    }
    Scaffold(
        topBar = {
            ServiceSelectionTopBar(navigateBack = { navigateBack() }, categoryName = categoryName)
        }
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            ServiceSelectionContent(
                navigateToUploadImage = { navigateToUploadImage() },
                sharedViewModel = sharedViewModel,
                serviceSelectionViewModel = serviceSelectionViewModel
            )
        }
    }
}

@Composable
fun ServiceSelectionContent(
    navigateToUploadImage: () -> Unit,
    sharedViewModel: SharedViewModel,
    serviceSelectionViewModel: ServiceSelectionViewModel
) {
    val context = LocalContext.current
    serviceSelectionViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> LoadingScreen()
            is UiState.Success -> ServiceSelectionList(
                services = uiState.data.service,
                navigateToUploadImage = navigateToUploadImage,
                sharedViewModel = sharedViewModel
            )

            is UiState.Error -> Toast.makeText(
                context,
                uiState.errorMessage,
                Toast.LENGTH_SHORT
            )
                .show()

        }
    }

}


@Composable
fun ServiceSelectionList(
    services: List<ServiceItem>,
    navigateToUploadImage: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyColumn {
        items(services) {
            ServiceMenuSelectionItem(
                serviceName = it.serviceName,
                serviceDescription = it.serviceDescription,
                price = it.price.toString(),
                onClick = {
                    sharedViewModel.addServiceId(it.id)
                    navigateToUploadImage()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSelectionTopBar(
    navigateBack: () -> Unit,
    categoryName: String
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "$categoryName's Services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Please select a service",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
