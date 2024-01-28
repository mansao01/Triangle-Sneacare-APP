package com.mansao.trianglesneacare.ui.screen.section.customer.maps

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun MapsScreen(
    sharedViewModel: SharedViewModel,
    mapsViewModel: MapsViewModel = hiltViewModel(),
    navigateToSearchAddress: () -> Unit
) {
    val context = LocalContext.current
    val predictionItem = sharedViewModel.predictionItem
    val placeId = predictionItem?.placeId ?: ""

    LaunchedEffect(key1 = placeId) {
        mapsViewModel.getLocationFromPlaceId(placeId)
    }
    Scaffold(
        topBar = { MapTopBar(navigateToSearchAddress = navigateToSearchAddress) },
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .navigationBarsPadding()
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            mapsViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
                when (uiState) {
                    is UiState.Standby -> {}
                    is UiState.Loading -> LoadingDialog()
                    is UiState.Success -> MapsScreenComponent(geocodingItem = uiState.data)
                    is UiState.Error -> Toast.makeText(
                        context,
                        uiState.errorMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    }
}

@Composable
fun MapsScreenComponent(
    geocodingItem: GeocodingResponse
) {
    Log.d("geocoding item", geocodingItem.toString())
    val northeastLat = geocodingItem.data.results[0].geometry.viewport.northeast.lat
    val northeastLng = geocodingItem.data.results[0].geometry.viewport.northeast.lng
    val southWestLat = geocodingItem.data.results[0].geometry.viewport.southwest.lat
    val southWestLng = geocodingItem.data.results[0].geometry.viewport.southwest.lng

    val centerLocationLat =
        geocodingItem.data.results[0].geometry.location.lat

    val centerLocationLng =
        geocodingItem.data.results[0].geometry.location.lng

    val centerLocation by remember {
        mutableStateOf(LatLng(centerLocationLat, centerLocationLng))
    }

    val boundsBuilder = LatLngBounds.builder()
        .include(LatLng(northeastLat, northeastLng))
        .include(LatLng(southWestLat, southWestLng))

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centerLocation, 17f)
    }
    val radius = calculateRadius(centerLocation, LatLng(northeastLat, northeastLng))

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState
        ) {

            boundsBuilder.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 64))

            Circle(
                center = centerLocation,
                radius = radius,
                fillColor = Color.Blue.copy(alpha = 0.3f),
                strokeColor = Color.Blue,
                strokeWidth = 2f
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                geocodingItem.data.results[0].formattedAddress?.let {
                    Text(text = it)
                }
                Button(
                    onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Select location coverage & continue to fill the address")

                }
            }

        }
    }
}


fun calculateRadius(center: LatLng, point: LatLng): Double {
    val result = FloatArray(1)
    Location.distanceBetween(
        center.latitude,
        center.longitude,
        point.latitude,
        point.longitude,
        result
    )
    return result[0].toDouble()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopBar(
    navigateToSearchAddress: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.location_scope))
        },
        navigationIcon = {

            IconButton(onClick = { navigateToSearchAddress() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        }
    )

}