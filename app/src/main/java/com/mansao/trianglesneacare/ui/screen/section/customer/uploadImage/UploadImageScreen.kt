package com.mansao.trianglesneacare.ui.screen.section.customer.uploadImage

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.mansao.trianglesneacare.R
import com.mansao.trianglesneacare.ui.common.UiState
import com.mansao.trianglesneacare.ui.components.AddedToCartDialog
import com.mansao.trianglesneacare.ui.screen.SharedViewModel
import com.mansao.trianglesneacare.utils.CameraUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UploadImageScreen(
    uploadImageViewModel: UploadImageViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    val serviceId = sharedViewModel.serviceId
    val categoryName = sharedViewModel.categoryName
    val context = LocalContext.current

    var isLoading by rememberSaveable {
        mutableStateOf(false)
    }

    UploadImageContent(
        navigateBack = { navigateBack() },
        categoryName = categoryName,
        context = context,
        isLoading = isLoading,
        uploadImageViewModel = uploadImageViewModel,
        serviceId = serviceId
    )

    uploadImageViewModel.uiState.collectAsState(initial = UiState.Standby).value.let { uiState ->
        when (uiState) {
            UiState.Standby -> {}
            UiState.Loading -> isLoading = true
            is UiState.Success -> {
                AddedToCartDialog(onClick = {
                    uploadImageViewModel.setStandbyState()
                    navigateToHome()
                })
                isLoading = false
            }

            is UiState.Error -> {
                Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
                uploadImageViewModel.setStandbyState()
                isLoading = false
            }
        }
    }
}

@Composable
fun UploadImageContent(
    navigateBack: () -> Unit,
    categoryName: String,
    context: Context,
    isLoading: Boolean,
    uploadImageViewModel: UploadImageViewModel,
    serviceId: String
) {
    val file = context.createImageFile()
    val cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    var captureImageUri by remember { mutableStateOf<Uri?>(null) }
    var isImageSelected by remember { mutableStateOf(false) }

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
        topBar = {
            UploadImageTopBar(navigateBack = navigateBack)
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier.padding(scaffoldPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Upload your $categoryName\'s image .",
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))
                DisplaySelectedImage(
                    imageUri = galleryImageUri ?: captureImageUri,
                    context = context
                )
                Spacer(modifier = Modifier.height(16.dp))

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
                Spacer(modifier = Modifier.height(16.dp))
                CreateOrderButton(
                    isLoading = isLoading,
                    imageUri = galleryImageUri ?: captureImageUri,
                    uploadImageViewModel = uploadImageViewModel,
                    serviceId = serviceId
                )
            }
        }

    }

}

@Composable
fun CreateOrderButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    imageUri: Uri?,
    uploadImageViewModel: UploadImageViewModel,
    serviceId: String
) {
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current
    val context = LocalContext.current
    val isButtonEnable = imageUri != null
    val myFile = imageUri?.let { CameraUtils.uriToFile(it, context) }
    val rotatedFile = myFile?.let { file ->
        CameraUtils.rotateFile(file)
    }

    OutlinedButton(
        onClick = {
            rotatedFile?.let {
                uploadImageViewModel.createOrder(serviceId, it)
            }
        },
        enabled = isButtonEnable,
        modifier = modifier
            .padding(top = 18.dp)
            .padding(horizontal = 16.dp)
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
            Text(text = stringResource(id = R.string.add_to_cart))
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
            modifier = Modifier.size(200.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_image),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadImageTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })


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