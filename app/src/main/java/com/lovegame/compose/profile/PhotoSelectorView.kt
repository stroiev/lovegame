package com.lovegame.compose.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.lovegame.R
import com.lovegame.viewmodels.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun PhotoSelectorView(
    onImageClick: (uri: Uri?) -> Unit,
    viewModel: ProfileViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val TAG = stringResource(R.string.app_name) + "Tag " + "PhotoSelectorView"
    val context = LocalContext.current
    val maxSelectionCount = 5
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    var remainingImages by rememberSaveable { mutableStateOf(maxSelectionCount) }
    var cameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    LaunchedEffect(key1 = viewModel.selectedImages.size) {
        remainingImages = maxSelectionCount - viewModel.selectedImages.size
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                cameraUri?.let{
                    viewModel.selectedImages.add(it)
                }
            } else {
                cameraUri?.let { viewModel.deleteUri(it, context) }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraUri = try {
                viewModel.getUri(context)
            } catch (ex: IOException) {
                Log.i(TAG, "Exception: $ex")
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.failed_to_create_image))
                }
                null
            }
            cameraLauncher.launch(cameraUri)
        } else {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.camera_permission_denied))
            }
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.selectedImages.add(it) }
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = if ((remainingImages) > 1) remainingImages
            else 2
        )
    ) { uris ->
        viewModel.selectedImages.addAll(uris)
    }

    fun launchPhotoPicker() {
        if ((remainingImages) > 1) {
            multiplePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    fun checkPermissionAndLaunch() {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraUri = try {
                viewModel.getUri(context)
            } catch (ex: IOException) {
                Log.i(TAG, "Exception: $ex")
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(context.getString(R.string.failed_to_create_image))
                }
                null
            }
            cameraLauncher.launch(cameraUri)
        } else {
            // Request permission
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(onClick = {
            if (remainingImages > 0) {
                launchPhotoPicker()
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.you_can_t_select_more_than_pictures, maxSelectionCount
                        )
                    )
                }
            }
        }) {
            Text(stringResource(R.string.select_photos))
        }
        Button(onClick = {
            if (remainingImages > 0) {
                checkPermissionAndLaunch()
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.you_can_t_select_more_than_pictures, maxSelectionCount
                        )
                    )
                }
            }

        }) {
            Text(text = stringResource(R.string.capture_image))
        }
    }

    if (viewModel.selectedImages.size > 0) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .height(150.dp)
        ) {
            items(viewModel.selectedImages) { uri ->
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = {
                                onImageClick(uri)
                            }),
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    IconButton(onClick = { viewModel.selectedImages.remove(uri) }) {
                        Icon(
                            Icons.TwoTone.Cancel,
                            "delete item",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}