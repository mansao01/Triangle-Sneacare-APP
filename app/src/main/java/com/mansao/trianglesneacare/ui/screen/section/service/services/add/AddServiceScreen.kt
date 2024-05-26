package com.mansao.trianglesneacare.ui.screen.section.service.services.add

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun AddServiceScreen(
    addServiceViewModel: AddServiceViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    val categoryId = sharedViewModel.categoryId
    val context = LocalContext.current
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    AddServiceComponent(
        addServiceViewModel = addServiceViewModel,
        navigateBack = navigateBack,
        isLoading = isLoading,
        categoryId = categoryId
    )
    addServiceViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            is UiState.Success -> {
                navigateBack()
                isLoading = false
            }

            UiState.Loading -> isLoading = true
            is UiState.Error -> {
                isLoading = false
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}

@Composable
fun AddServiceComponent(
    addServiceViewModel: AddServiceViewModel,
    categoryId: String,
    navigateBack: () -> Unit,
    isLoading: Boolean
) {
    var serviceName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }

    val isButtonEnable =
        (serviceName.isNotEmpty() && price.isNotEmpty() && serviceDescription.isNotEmpty())
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current


    Scaffold(
        topBar = { AddCategoryTopBar(navigateBack = navigateBack) }
    ) { scaffoldPadding ->
        Surface(modifier = Modifier.padding(scaffoldPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)

                Text(
                    text = stringResource(R.string.add_service),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    modifier = modifier
                )

                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.service_name
                            )
                        )
                    },
                    maxLines = 1,
                    modifier = modifier
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it},
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.price
                            )
                        )
                    },
                    maxLines = 1,
                    modifier = modifier
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = serviceDescription,
                    onValueChange = { serviceDescription = it },
                    label = {
                        Text(
                            text = stringResource(
                                id = R.string.service_description
                            )
                        )
                    },
                    modifier = modifier
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = {
                        addServiceViewModel.addService(
                            serviceName,
                            price,
                            categoryId,
                            serviceDescription
                        )
                    },
                    modifier = modifier
                        .padding(top = 18.dp)
                        .fillMaxWidth()
                        .then(
                            if (buttonSize != DpSize.Zero) Modifier.size(buttonSize) else Modifier
                        )
                        .onSizeChanged { newSize ->
                            if (buttonSize == DpSize.Zero) {
                                buttonSize = with(density) {
                                    newSize
                                        .toSize()
                                        .toDpSize()
                                }
                            }
                        },
                    enabled = isButtonEnable
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                        )
                    } else {
                        Text(text = stringResource(id = R.string.create))
                    }

                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryTopBar(
    navigateBack: () -> Unit
) {
    LargeTopAppBar(title = { /*TODO*/ }, navigationIcon = {
        IconButton(onClick = { navigateBack() }) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
    })

}