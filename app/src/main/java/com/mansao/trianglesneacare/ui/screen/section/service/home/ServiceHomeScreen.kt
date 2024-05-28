package com.mansao.trianglesneacare.ui.screen.section.service.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.components.HeaderText


@Composable
fun ServiceHomeScreen() {
    ServiceHome()
}

@Composable
fun ServiceHome(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { ServiceHomeTopBar() },
    ) { scaffoldPadding ->
        Surface(modifier.padding(scaffoldPadding)) {

        }
    }
}

@Composable
fun ServiceHomeContent(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceHomeTopBar(modifier: Modifier = Modifier) {
    LargeTopAppBar(title = {
        HeaderText(
            text = stringResource(R.string.orders),
            description = "",
            showDescription = false
        )
    })
}