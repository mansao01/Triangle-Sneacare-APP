package com.mansao.trianglesneacare.ui.screen.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
    val role = profile.profile.role.name
    Column(modifier = Modifier.fillMaxSize()) {
        UserDetailComponent(
            image = profile.profile.image,
            name = profile.profile.name,
            email = profile.profile.email,
            phone = profile.profile.phone,
            modifier = Modifier.clickable { TODO() }
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(
                    color = Color.Gray,
                    shape = RectangleShape,
                )
                .fillMaxWidth()
        )

        if (role == "customer") {
            ProfileMenuItem(
                menuName = "Transaction List",
                icon = Icons.Outlined.ListAlt,
                onClick = { TODO() }
            )
        }


        Spacer(modifier = Modifier.height(8.dp))
        ProfileMenuItem(
            menuName = "Logout",
            icon = Icons.Outlined.Logout,
            onClick = { viewModel.logout() }
        )
        Spacer(modifier = Modifier.height(8.dp))


    }
}

@Composable
fun ProfileMenuItem(
    menuName: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp)
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )

        // Spacer
        Spacer(modifier = Modifier.width(16.dp))

        // Menu Name
        Text(
            text = menuName,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        )

    }
}

@Composable
fun UserDetailComponent(
    image: String?,
    name: String,
    email: String,
    phone: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
                if (image == null) {
                    Image(
                        painter = painterResource(id = R.drawable.person_circle_svgrepo_com),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
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

                phone?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    )
                }


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

