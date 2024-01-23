package com.mansao.trianglesneacare.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
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
        UserDetailComponent(
            image = profile.profile.image,
            name = profile.profile.name,
            email = profile.profile.email
        )
        Button(onClick = { viewModel.logout() }) {
            Text(text = stringResource(R.string.logout))
        }
    }
}

@Composable
fun UserDetailComponent(
    image: String?,
    name: String,
    email: String
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(93.dp)
                    .clip(CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .crossfade(true)
                        .data(image)
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
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = email,
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}
