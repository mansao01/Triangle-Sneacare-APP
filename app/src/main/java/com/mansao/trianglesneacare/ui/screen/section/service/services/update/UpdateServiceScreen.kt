package com.mansao.trianglesneacare.ui.screen.section.service.services.update

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.data.network.request.UpdateServiceRequest
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.LoadingDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel

@Composable
fun UpdateServiceScreen(
    modifier: Modifier = Modifier,
    updateServiceViewModel: UpdateServiceViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit
) {
    val serviceId = sharedViewModel.updateServiceArgs?.id ?: ""
    val emptyService = UpdateServiceRequest("", "", 0, "")
    val service = sharedViewModel.updateServiceArgs
    val context = LocalContext.current
    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }
    UpdateService(
        modifier = modifier,
        serviceId = serviceId,
        updateServiceViewModel = updateServiceViewModel,
        navigateBack = navigateBack,
        service = service ?: emptyService,
        isLoading = isLoading
    )
    updateServiceViewModel.isDeleteSuccess.collectAsState(initial = false).value.let { isDeleteSuccess ->
        if (isDeleteSuccess) {
            navigateBack()
            updateServiceViewModel.deleteMessage.collectAsState(initial = "").value.let { deleteMessage ->
                Toast.makeText(context, deleteMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    updateServiceViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->

        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> LoadingDialog()
            is UiState.Success -> {
                LaunchedEffect(Unit) {
                    isLoading = false
                    navigateBack()
                    Toast.makeText(context, uiState.data.msg, Toast.LENGTH_SHORT).show()
                }
            }

            is UiState.Error -> {
                isLoading = false
                LaunchedEffect(Unit) {
                    Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}

@Composable
fun UpdateService(
    modifier: Modifier = Modifier,
    serviceId: String,
    updateServiceViewModel: UpdateServiceViewModel,
    service: UpdateServiceRequest,
    navigateBack: () -> Unit,
    isLoading: Boolean
) {
    Scaffold(
        topBar = {
            UpdateServiceTopBar(
                updateServiceViewModel = updateServiceViewModel,
                serviceId = serviceId,
                navigateBack = navigateBack,
            )
        }
    ) { paddingValues ->
        Surface(modifier = modifier.padding(paddingValues)) {
            UpdateServiceContent(
                updateServiceViewModel = updateServiceViewModel,
                service = service,
                isLoading = isLoading
            )
        }
    }

}

@Composable
fun UpdateServiceContent(
    service: UpdateServiceRequest,
    updateServiceViewModel: UpdateServiceViewModel,
    isLoading: Boolean

) {
    val id by remember { mutableStateOf(service.id) }
    var name by remember { mutableStateOf(service.serviceName) }
    var description by remember { mutableStateOf(service.serviceDescription) }
    var price by remember { mutableStateOf(service.price.toString()) }

    val isButtonEnable =
        (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty())
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current

    Column {
        val modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)

        Text(
            text = stringResource(R.string.add_service),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = modifier
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
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
            onValueChange = { price = it },
            label = {
                Text(
                    text = stringResource(
                        id = R.string.price
                    )
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
            ),
            maxLines = 1,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
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
                updateServiceViewModel.updateService(
                    UpdateServiceRequest(
                        id,
                        name,
                        price.toInt(),
                        description
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateServiceTopBar(
    updateServiceViewModel: UpdateServiceViewModel,
    serviceId: String,
    navigateBack: () -> Unit,
) {


    TopAppBar(
        title = { Text(text = "Update Service") },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = {
                updateServiceViewModel.deleteService(serviceId)
            }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "")
            }
        })
}