package com.mansao.trianglesneacare.ui.screen.section.admin.driverRegistrarion

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.DriverRegistrationUiState
import com.mansao.trianglesneacare.ui.components.HeaderText
import com.mansao.trianglesneacare.utils.CameraUtils
import com.mansao.trianglesneacare.utils.CommonUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DriverRegistrationScreen(
    driverRegistrationViewModel: DriverRegistrationViewModel = hiltViewModel(),
    navigateToDriverManagement: () -> Unit
) {
    val uiState = driverRegistrationViewModel.uiState
    DriverRegisterComponent(
        driverRegistrationViewModel = driverRegistrationViewModel,
        navigateToDriverManagement = { navigateToDriverManagement() },
        uiState = uiState
    )

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DriverRegisterComponent(
    driverRegistrationViewModel: DriverRegistrationViewModel,
    navigateToDriverManagement: () -> Unit,
    uiState: DriverRegistrationUiState,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    when (uiState) {
        is DriverRegistrationUiState.Standby -> {}
        is DriverRegistrationUiState.Loading -> {
            isLoading = true
        }

        is DriverRegistrationUiState.Success -> {
            LaunchedEffect(key1 = Unit) {
                snackbarHostState.showSnackbar(message = uiState.registerResponse.msg)
                isLoading = false
            }
        }

        is DriverRegistrationUiState.Error -> {
            LaunchedEffect(key1 = Unit) {
                snackbarHostState.showSnackbar(message = uiState.msg)
                isLoading = false
            }
        }
    }
    val context = LocalContext.current

    val file = context.createImageFile()
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var captureImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageSelected by remember { mutableStateOf(false) }


    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        galleryImageUri = uri
        isImageSelected = true

    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { isSuccess ->
        if (isSuccess) {
            captureImageUri = cameraUri
            isImageSelected = true

        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(cameraUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { DriverRegistrationTopBar(navigateToDriverManagement = navigateToDriverManagement) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DisplaySelectedImage(
                    imageUri = galleryImageUri ?: captureImageUri,
                    context = context
                )

                if (isImageSelected) {
                    Button(onClick = {
                        captureImageUri = null
                        galleryImageUri = null
                        isImageSelected = false
                    }) {
                        Text(text = "Remove Image")
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            openGallery(launcher = galleryLauncher)
                        }) {
                            Text(text = "Open Gallery")
                        }
                        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                        Button(onClick = {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(cameraUri)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }) {
                            Text(text = "Open Camera")
                        }
                    }
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(text = stringResource(R.string.name)) },
                    placeholder = { Text(text = stringResource(R.string.name)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text(text = stringResource(R.string.enter_your_email)) },
                    placeholder = { Text(text = stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (CommonUtils.isEmailValid(email)) {
                                keyboardController?.hide()
                            }
                        }
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = stringResource(R.string.enter_your_password)) },
                    placeholder = { Text(text = stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 16.dp)
                        ) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisibility) {
                                    stringResource(R.string.hide_password)
                                } else {
                                    stringResource(R.string.show_password)
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it.trim() },
                    label = { Text(text = stringResource(R.string.address)) },
                    placeholder = { Text(text = stringResource(R.string.address)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.trim() },
                    label = { Text(text = stringResource(R.string.phone)) },
                    placeholder = { Text(text = stringResource(R.string.phone)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                )

                RegisterButton(
                    imageUri = galleryImageUri ?: captureImageUri,
                    driverRegistrationViewModel = driverRegistrationViewModel,
                    name = name,
                    email = email,
                    password = password,
                    address = address,
                    phone = phone,
                    isLoading = isLoading
                )
            }
        }
    }
}


@Composable
fun RegisterButton(
    imageUri: Uri?,
    driverRegistrationViewModel: DriverRegistrationViewModel,
    name: String,
    email: String,
    password: String,
    address: String,
    phone: String,
    isLoading: Boolean
) {
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current
    val context = LocalContext.current
    val isButtonEnable =
        (name.isNotEmpty() && imageUri != null && email.isNotEmpty() && password.isNotEmpty() && address.isNotEmpty() && phone.isNotEmpty())
    val myFile = imageUri?.let { CameraUtils.uriToFile(it, context) }
    val rotatedFile = myFile?.let { file ->
        CameraUtils.rotateFile(file)
    }

    Button(
        onClick = {
            rotatedFile?.let {
                driverRegistrationViewModel.registerAsDriver(
                    name = name,
                    email = email,
                    password = password,
                    address = address,
                    phone = phone,
                    file = it

                )

            }
        },
        enabled = isButtonEnable,
        modifier = Modifier
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
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        } else {
            Text(stringResource(id = R.string.register))
        }
    }
}

@Composable
fun DisplaySelectedImage(imageUri: Uri?, context: Context) {

    if (imageUri != null) {
        val bitmap = if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        }
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
    }

}

fun openGallery(launcher: ActivityResultLauncher<String>) {
    launcher.launch("image/*")
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriverRegistrationTopBar(
    navigateToDriverManagement: () -> Unit
) {
    TopAppBar(title = { HeaderText(text = "Driver Register", showDescription = false) },
        navigationIcon = {
            IconButton(
                onClick = { navigateToDriverManagement() }
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
        })
}