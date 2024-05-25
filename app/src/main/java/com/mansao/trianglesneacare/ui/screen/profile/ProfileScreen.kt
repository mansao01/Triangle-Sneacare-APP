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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.ProfileMenuItem

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfileEdit: () -> Unit,
    navigateToAddressList: () -> Unit
) {
    val context = LocalContext.current
    when (uiState) {
        is ProfileUiState.Loading -> LoadingDialog()
        is ProfileUiState.Success -> ProfileComponent(
            profile = uiState.profile,
            viewModel = profileViewModel,
            navigateToProfileEdit = navigateToProfileEdit,
            navigateToAddressList = navigateToAddressList
        )

        is ProfileUiState.Error -> Toast.makeText(context, uiState.msg, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ProfileComponent(
    profile: ProfileResponse,
    viewModel: ProfileViewModel,
    navigateToProfileEdit: () -> Unit,
    navigateToAddressList: () -> Unit
) {
    val role = profile.profile.role.role
    Column(modifier = Modifier.fillMaxSize()) {
        UserDetail(
            image = profile.profile.image,
            name = profile.profile.name,
            email = profile.profile.email,
            phone = profile.profile.phone,
            modifier = Modifier.clickable { navigateToProfileEdit() }
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
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.account_setting),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (role == "customer") {

            ProfileMenuItem(
                menuName = stringResource(R.string.address_list),
                icon = Icons.Outlined.Home,
                onClick = { navigateToAddressList() }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        ProfileMenuItem(
            menuName = stringResource(R.string.logout),
            icon = Icons.Outlined.Logout,
            onClick = { viewModel.logout() }
        )
        Spacer(modifier = Modifier.height(8.dp))


    }
}

@Composable
fun UserDetail(
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
            Spacer(modifier = Modifier.width(32.dp))
            Icon(imageVector = Icons.Outlined.Edit, contentDescription = null )
        }
    }
}

