package com.mansao.trianglesneacare.ui.screen.section.customer.maps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.local.model.LocationDetail
import com.mansao.trianglesneacare.data.network.response.GeocodingResponse
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MapsScreen(
    sharedViewModel: SharedViewModel,
    mapsViewModel: MapsViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToAddScreen: () -> Unit
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
                MapsScreenComponentWIthLocation(
                    mapProperties = mapProperties,
                    sharedViewModel = sharedViewModel,
                    navigateToAddScreen = navigateToAddScreen
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
                            mapProperties = mapProperties
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
    mapProperties: MapProperties
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
                    onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.button_with_place_id))

                }

            }

        }
    }
}


@Composable
fun MapsScreenComponentWIthLocation(
    mapProperties: MapProperties,
    sharedViewModel: SharedViewModel,
    navigateToAddScreen: () -> Unit

) {
    val context = LocalContext.current
    val location by remember { mutableStateOf(sharedViewModel.location) }
    val latitude = location?.latitude ?: 0.0
    val longitude = location?.longitude ?: 0.0
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 18f)
    }
    val detailLocation = getDetailLocation(latitude, longitude, context)

    Log.d("detail location", detailLocation.toString())

    Box(modifier = Modifier.fillMaxWidth()) {
        GoogleMap(
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(LatLng(latitude, longitude))
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
                Text(text = detailLocation.address)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        sharedViewModel.addFullAddress(detailLocation.address)
                        Log.d("full address",detailLocation.address)
                        navigateToAddScreen()
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.button_with_get_location))

                }

            }

        }
    }
}

fun getDetailLocation(latitude: Double, longitude: Double, context: Context): LocationDetail {
    val geocoder = Geocoder(context, Locale.getDefault())

    var locationDetail = LocationDetail()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(
            latitude, longitude, 1
        ) { addresses ->
            val address = addresses[0]
            val addressText = address.getAddressLine(0) ?: ""
            val city = address.locality ?: ""
            val state = address.adminArea ?: ""
            val country = address.countryName ?: ""
            val subCity = address.subAdminArea ?: "" //kabupaten
            val village = address.subLocality ?: "" //desa
            val roadName = address.thoroughfare ?: ""

            locationDetail =
                LocationDetail(addressText, city, state, country, subCity, village, roadName)
        }
    } else {
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            return if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                Log.d("full location", address.toString())

                LocationDetail(
                    address.getAddressLine(0) ?: "",
                    address.locality ?: "",
                    address.adminArea ?: "",
                    address.countryName ?: "",
                    address.subAdminArea ?: "",
                    address.subLocality ?: "",
                    address.thoroughfare ?: ""
                )
            } else {
                LocationDetail("", "", "", "", "", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            locationDetail = LocationDetail(
                address = "Error retrieving location details"

            )
        }
    }

    return locationDetail
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
//            stringResource(R.string.location_scope)

        },
        navigationIcon = {

            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        }
    )

}