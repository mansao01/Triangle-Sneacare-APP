package com.mansao.trianglesneacare.ui.screen.section.service.services

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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.data.network.response.ServiceItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.ServiceMenuItem
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel = hiltViewModel(),
    navigateToAddService: () -> Unit,
    navigateBack: () -> Unit,
    navigateToUpdateService: () -> Unit,
    sharedViewModel: SharedViewModel

) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val categoryId = sharedViewModel.categoryId
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getServicesByCategoryId(categoryId)

    }
    val role = viewModel.role.collectAsState(initial = "").value


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
                    navigateBack = navigateBack,
                    navigateToUpdateService = navigateToUpdateService,
                    sharedViewModel = sharedViewModel,
                    scrollBehavior = scrollBehavior,
                    role = role
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesContent(
    services: List<ServiceItem>,
    navigateToAddService: () -> Unit,
    navigateToUpdateService: () -> Unit,
    navigateBack: () -> Unit,
    sharedViewModel: SharedViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    role: String

) {
    Scaffold(
        topBar = {
            ServicesTopBar(
                navigateToAddService = navigateToAddService,
                navigateBack = navigateBack,
                scrollBehavior = scrollBehavior,
                role = role
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            ServiceList(
                services = services,
                navigateToUpdateService = navigateToUpdateService,
                sharedViewModel = sharedViewModel
            )
        }
    }
}

@Composable
fun ServiceList(
    modifier: Modifier = Modifier,
    services: List<ServiceItem>,
    navigateToUpdateService: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyColumn(modifier = modifier) {
        items(services) {
            ServiceMenuItem(
                serviceName = it.serviceName,
                price = it.price.toString(),
                serviceDescription = it.serviceDescription,
                onClick = {
                    navigateToUpdateService()
                    sharedViewModel.addUpdateServiceArgs(
                        UpdateServiceRequest(
                            it.id,
                            it.serviceName,
                            it.price,
                            it.serviceDescription
                        )
                    )
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesTopBar(
    navigateToAddService: () -> Unit,
    navigateBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    role: String
) {
    LargeTopAppBar(
        scrollBehavior = scrollBehavior,
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
            if (role == "service"){
                IconButton(
                    onClick = { navigateToAddService() }) {
                    Icon(imageVector = Icons.Outlined.Add, contentDescription = null)

                }
            }
        })
}


