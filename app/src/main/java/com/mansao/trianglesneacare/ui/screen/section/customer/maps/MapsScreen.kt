package com.mansao.trianglesneacare.ui.screen.section.customer.maps

import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapsScreen(
    sharedViewModel: SharedViewModel,
    mapsViewModel: MapsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToAddAddress: () -> Unit
) {
    val context = LocalContext.current
    val predictionItem = sharedViewModel.predictionItem
    val placeId = predictionItem?.placeId ?: ""
    val mapProperties = MapProperties(
        mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.maps_style)
    )

    Scaffold(
        topBar = { MapTopBar(navigateBack = navigateBack, placeId = placeId) },
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Surface {
            if (placeId.isEmpty()) {
                MapsScreenComponentWithLocation(
                    mapProperties = mapProperties,
                    sharedViewModel = sharedViewModel,
                    navigateToAddAddress = navigateToAddAddress,
                    mapsViewModel = mapsViewModel
                )
            } else {
                LaunchedEffect(key1 = placeId) {
                    mapsViewModel.getLocationFromPlaceId(placeId)
                }
                mapsViewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
                    when (uiState) {
                        is UiState.Standby -> {}
                        is UiState.Loading -> LoadingDialog()
                        is UiState.Success -> MapsScreenComponentWithPlaceId(
                            geocodingItem = uiState.data,
                            mapProperties = mapProperties,
                            navigateToAddAddress = navigateToAddAddress
                        )

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
}

@Composable
fun MapsScreenComponentWithPlaceId(
    geocodingItem: GeocodingResponse,
    mapProperties: MapProperties,
    navigateToAddAddress: () -> Unit
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
        position = CameraPosition.fromLatLngZoom(centerLocation, 10f)
    }

    val radius = calculateRadius(centerLocation, LatLng(northeastLat, northeastLng))

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            properties = mapProperties
        ) {
            boundsBuilder.build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 64))

            Circle(
                center = centerLocation,
                radius = radius,
                fillColor = Color.White.copy(alpha = 0.3f),
                strokeColor = Color.White,
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
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { navigateToAddAddress() }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.button_with_place_id))

                }

            }

        }
    }
}


@OptIn(FlowPreview::class)
@Composable
fun MapsScreenComponentWithLocation(
    mapProperties: MapProperties,
    sharedViewModel: SharedViewModel,
    mapsViewModel: MapsViewModel,
    navigateToAddAddress: () -> Unit
) {
    val context = LocalContext.current
    val location by remember { mutableStateOf(sharedViewModel.location) }
    val latitude by remember { mutableDoubleStateOf(location?.latitude ?: 0.0) }
    val longitude by remember { mutableDoubleStateOf(location?.longitude ?: 0.0) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 18f)
    }
    LaunchedEffect(Unit) {
        mapsViewModel.getDetailLocation(latitude, longitude, context)
    }
    val markerState = rememberMarkerState(position = LatLng(latitude, longitude))
    val newLatitude = markerState.position.latitude
    val newLongitude = markerState.position.longitude

    val debouncedAddress = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mapsViewModel.address
            .debounce(1000)
            .collect { newAddress ->
                debouncedAddress.value = newAddress
            }
    }

    val balloonBuilder = rememberBalloonBuilder {
        setArrowSize(10)
        setArrowPosition(0.5f)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
    }

    Log.d("lat", latitude.toString())

    Box(modifier = Modifier.fillMaxWidth()) {
        GoogleMap(
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = markerState,
                draggable = true
            )

        }
        LaunchedEffect(key1 = newLatitude, key2 = newLongitude) {
            Log.d("marker", "New coordinates: $newLatitude, $newLongitude")
            mapsViewModel.getDetailLocation(newLatitude, newLongitude, context)
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

                Balloon(
                    builder = balloonBuilder,
                    balloonContent = {
                        Column {
                            Text(text = "You can move the marker by hold and move the marker")
                            OutlinedButton(
                                onClick = {
                                    mapsViewModel.hideBalloon()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Dismiss")
                            }
                        }
                    }
                ) { balloonWindow ->
                    mapsViewModel.showBalloonState.collectAsState().value.let { showBalloon ->
                        Log.d("balloonState", showBalloon.toString())
                        if (showBalloon) balloonWindow.showAlignTop() else balloonWindow.dismiss()

                    }

                    Text(text = debouncedAddress.value)
                }

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedButton(
                    onClick = {
                        sharedViewModel.addFullAddress(debouncedAddress.value)
                        Log.d("full address", debouncedAddress.value)
                        navigateToAddAddress()
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.button_with_get_location))
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
    navigateBack: () -> Unit,
    placeId: String
) {

    TopAppBar(
        title = {
            Text(
                text = if (placeId.isNotEmpty()) stringResource(R.string.location_scope) else stringResource(
                    R.string.your_location
                )
            )
        },
        navigationIcon = {

            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )

}