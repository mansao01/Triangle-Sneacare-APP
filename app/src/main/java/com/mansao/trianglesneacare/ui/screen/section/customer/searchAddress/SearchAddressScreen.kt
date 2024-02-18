package com.mansao.trianglesneacare.ui.screen.section.customer.searchAddress

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.PredictionsItem
import com.mansao.trianglesneacare.location.LocationCallback
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AddressNotFound
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.components.LocationCheckingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel


@Composable
fun SearchAddressScreen(
    navigateBack: () -> Unit,
    navigateToMap: () -> Unit,
    searchViewModel: SearchAddressViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var showLocationAlert by remember { mutableStateOf(false) } // State variable for showing location alert
//    var showLocationAlert = false
    searchViewModel.gpsProviderState.collectAsState(initial = false).value.let { isGpsActive ->


        if (showLocationAlert) {
            LocationCheckingDialog(action = {
                searchViewModel.isGpsActive(context)
                showLocationAlert = false
            })
        }

        LaunchedEffect(isGpsActive) {
            searchViewModel.isGpsActive(context)
        }
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.all { it }
            if (areGranted) {
                searchViewModel.isGpsActive(context)
                if (isGpsActive) {
                    searchViewModel.setLoadingState()
                    getCurrentLocation(fusedLocationClient, object : LocationCallback {
                        override fun onLocationResult(location: Location) {
                            val latitude = location.latitude
                            val longitude = location.longitude
                            searchViewModel.setStandbyState()
                            navigateToMap()
                            sharedViewModel.addLocation(latitude, longitude)
                        }
                    })
                }
            } else {
                // hey future me, maybe u can give a alert dialog to warn customer, they need it to use the feature
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        Scaffold(
            topBar = { SearchAddressTopBar(navigateBack = navigateBack) }
        ) {
            Surface(modifier = Modifier.padding(it)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SearchBarAddressComponent(searchViewModel = searchViewModel)
                    Spacer(modifier = Modifier.height(8.dp))

                    ListItem(
                        headlineContent = {
                            Text(
                                text = "Use my current location",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.LocationSearching,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.clickable {
                            if (!isGpsActive && !showLocationAlert) { // Update the condition
                                showLocationAlert = true // Set the alert flag
                            }
                            searchViewModel.isGpsActive(context)
                            if (hasLocationPermissions(context, permissions)) {
                                if (isGpsActive) {
                                    searchViewModel.setLoadingState()
                                    getCurrentLocation(
                                        fusedLocationClient,
                                        object : LocationCallback {
                                            override fun onLocationResult(location: Location) {
                                                val latitude = location.latitude
                                                val longitude = location.longitude
                                                navigateToMap()
                                                sharedViewModel.addLocation(latitude, longitude)
                                                searchViewModel.setStandbyState()
                                            }
                                        })
                                }

                            } else {
                                launcherMultiplePermissions.launch(permissions)
                            }
                        }
                    )

                    searchViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                        when (uiState) {
                            is UiState.Standby -> {}
                            is UiState.Loading -> LoadingDialog()
                            is UiState.Success -> SearchAddressComponent(
                                addressPrediction = uiState.data.data.predictions,
                                navigateToMap = navigateToMap,
                                sharedViewModel = sharedViewModel
                            )

                            is UiState.Error -> {}
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchAddressComponent(
    addressPrediction: List<PredictionsItem>,
    navigateToMap: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    if (addressPrediction.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
        ) {

            items(addressPrediction) { item ->
                ListItem(
                    headlineContent = {
                        item.structuredFormatting.mainText?.let {
                            Text(
                                text = it,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                        )
                    },
                    supportingContent = {
                        item.structuredFormatting.secondaryText?.let { Text(text = it) }
                    },
                    modifier = Modifier.clickable {
                        sharedViewModel.addPlace(newPredictionItem = item)
                        Log.d("item", item.toString())
                        navigateToMap()
                    }

                )
            }
        }
    } else {
        AddressNotFound()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAddressTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.search_address),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.where_is_your_delivery_location),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null
                )
            }

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarAddressComponent(
    searchViewModel: SearchAddressViewModel,
    modifier: Modifier = Modifier
) {
    var query by rememberSaveable { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = { newQuery ->
            query = newQuery
            isActive = if (newQuery.isNotEmpty()) {
                searchViewModel.autoCompleteAddress(newQuery)
                true
            } else {
                false
            }
        },
        onSearch = { searchViewModel.autoCompleteAddress(query) },
        onActiveChange = { isActive = it },
        active = false,
        placeholder = {
            Text(
                text = stringResource(R.string.search_location_placeholder),
                fontSize = 12.sp
            )
        },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (isActive && query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        query = ""
                        searchViewModel.autoCompleteAddress(query)
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                }
            }
        },
        content = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}


private fun hasLocationPermissions(context: Context, permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    callback: LocationCallback
) {
    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object :
        CancellationToken() {
        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
            CancellationTokenSource().token

        override fun isCancellationRequested(): Boolean = false
    }).addOnSuccessListener { location: Location? ->
        if (location != null) {
            callback.onLocationResult(location)
        }
    }
}

