package com.mansao.trianglesneacare.ui.screen.section.admin.services

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
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
import com.mansao.trianglesneacare.data.network.response.ServiceItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.ServiceMenuItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel = hiltViewModel(),
    categoryId: Int,
    navigateToAddService: (Int) -> Unit,
    navigateBack: () -> Unit

) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getServicesByCategoryId(categoryId)

    }

    viewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

            UiState.Loading -> LoadingDialog()
            UiState.Standby -> {}
            is UiState.Success -> {
                ServicesContent(
                    services = uiState.data.service,
                    navigateToAddService = navigateToAddService,
                    categoryId = categoryId,
                    navigateBack = navigateBack
                )
            }


        }
    }
}

@Composable
fun ServicesContent(
    services: List<ServiceItem>,
    navigateToAddService: (Int) -> Unit,
    categoryId: Int,
    navigateBack: () -> Unit

) {
    Scaffold(
        topBar = {
            ServicesTopBar(
                navigateToAddService = navigateToAddService,
                categoryId = categoryId,
                navigateBack = navigateBack
            )
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            LazyColumn {
                items(services) {
                    ServiceMenuItem(serviceName = it.serviceName.toString())
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesTopBar(
    navigateToAddService: (Int) -> Unit,
    categoryId: Int,
    navigateBack: () -> Unit
) {
    LargeTopAppBar(
        title = {
            HeaderText(
                text = "Services",
                description = "",
                showDescription = false
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { navigateToAddService(categoryId) }) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)

            }
        })
}


