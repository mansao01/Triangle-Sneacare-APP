package com.mansao.trianglesneacare.ui.screen.section.customer.home

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomerHomeScreen() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeTopBar() {
    CenterAlignedTopAppBar(title = { Text(text = "Triangle Sneacare") })
}