package com.mansao.trianglesneacare.ui.screen.profileEdit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.GetProfileDetailResponse
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog

@Composable
fun ProfileEditScreen(
    profileEditViewModel: ProfileEditViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current

    profileEditViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Standby -> {}
            is UiState.Loading -> LoadingDialog()
            is UiState.Success -> ProfileEditComponent(uiState.data)
            is UiState.Error -> Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                .show()

        }

    }
}

@Composable
fun ProfileEditComponent(
    profile: GetProfileDetailResponse
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image with circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(color = Color.Gray)
        ) {
            // Load the image into the circle
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .crossfade(true)
                    .data(profile.user.image)
                    .build(),
                contentDescription = null,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


        }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Name
        OutlinedTextField(
            value = profile.user.name,
            onValueChange = {},
            label = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Editable Phone
        OutlinedTextField(
            value = profile.user.phone ?: "",
            onValueChange = {},
            label = { Text(text = "Phone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                // Handle save or update logic
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = stringResource(R.string.update))
        }
    }
}