package com.mansao.trianglesneacare.ui.screen.section.admin.driverManagement

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.mansao.trianglesneacare.data.network.response.DriversItem
import com.mansao.trianglesneacare.ui.common.DriverManagementUiState
import com.mansao.trianglesneacare.ui.components.DriverListItem
import com.mansao.trianglesneacare.ui.components.EmptyData
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@Composable
fun DriverManagementScreen(
    uiState: DriverManagementUiState,
    navigateToDriverRegistration: () -> Unit,

) {
    val context = LocalContext.current

    when (uiState) {
        is DriverManagementUiState.Loading -> LoadingDialog()
        is DriverManagementUiState.Success -> DriverManagementComponent(
            driver = uiState.getDriversResponse.drivers,
            navigateToDriverRegistration
        )

        is DriverManagementUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT)
            .show()
    }
}

@Composable
fun DriverManagementComponent(
    driver: List<DriversItem>,
    navigateToDriverRegistration: () -> Unit

) {
    Scaffold(
        topBar = { DriverManagementTopAppBar() },
        floatingActionButton = { DriverManagementFAB(navigateToDriverRegistration = navigateToDriverRegistration) }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            if (driver.isNotEmpty()) {
                DriverList(driver = driver)
            } else {
                EmptyData()
            }
        }
    }
}

@Composable
fun DriverList(
    driver: List<DriversItem>
) {
    LazyColumn {
        items(driver) { data ->
            DriverListItem(driversItem = data, modifier = Modifier.clickable { })
        }
    }
}

@Composable
fun DriverManagementFAB(
    navigateToDriverRegistration: () -> Unit
) {
    FloatingActionButton(onClick = { navigateToDriverRegistration() }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverManagementTopAppBar() {
    LargeTopAppBar(title = { HeaderText(text = "Driver ", description = "", showDescription = false) })
}
