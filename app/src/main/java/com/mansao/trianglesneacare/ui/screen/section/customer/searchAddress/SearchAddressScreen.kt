package com.mansao.trianglesneacare.ui.screen.section.customer.searchAddress

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.response.PredictionsItem
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AutoCompleteSearchAddressListItem
import com.mansao.trianglesneacare.ui.components.LoadingScreen

@Composable
fun SearchAddressScreen(
    navigateToAddressList: () -> Unit,
    searchViewModel: SearchAddressViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = { SearchAddressTopBar(navigateToAddressList = navigateToAddressList) }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SearchBarAddressComponent(searchViewModel = searchViewModel)
                Spacer(modifier = Modifier.height(12.dp))
                searchViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
                    when (uiState) {
                        is UiState.Standby -> {}
                        is UiState.Loading -> LoadingScreen()
                        is UiState.Success -> SearchAddressComponent(addressPrediction = uiState.data.data.predictions)
                        is UiState.Error -> {}
                    }
                }
            }
        }
    }

}

@Composable
fun SearchAddressComponent(
    addressPrediction: List<PredictionsItem>
) {
    LazyColumn {
        items(addressPrediction) { item ->
            AutoCompleteSearchAddressListItem(item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAddressTopBar(
    navigateToAddressList: () -> Unit
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
                    fontSize = 14.sp
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navigateToAddressList() }) {
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