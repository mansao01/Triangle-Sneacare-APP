package com.mansao.trianglesneacare.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.ProfileResponse
import com.mansao.trianglesneacare.ui.common.ProfileUiState
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    when (uiState) {
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Success -> ProfileComponent(
            profile = uiState.profile,
            viewModel = profileViewModel
        )

        is ProfileUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ProfileComponent(
    profile: ProfileResponse,
    viewModel: ProfileViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        UserDetailComponent(image = profile.profile.name, name =profile.profile.name , email = profile.profile.email)
        Button(onClick = { viewModel.logout() }) {
            Text(text = stringResource(R.string.logout))
        }
    }
}

@Composable
fun UserDetailComponent(
    image:String,
    name:String,
    email:String
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            model = ImageRequest.Builder(context)
                .crossfade(true)
                .data(image)
                .build(),
            contentDescription = null
        )
        Column {
            Text(text = name)
            Text(text = email)
        }
    }

}