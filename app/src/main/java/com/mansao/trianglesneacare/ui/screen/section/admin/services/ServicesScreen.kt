package com.mansao.trianglesneacare.ui.screen.section.admin.services

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ServicesScreen(
    viewModel: ServicesViewModel = hiltViewModel(),
    categoryId: Int
) {
    LaunchedEffect(Unit) {
        viewModel.getServicesByCategoryId(categoryId)

    }
    Box(
        contentAlignment = Alignment.Center,
    ) {

        Text(text = categoryId.toString())
    }
}




