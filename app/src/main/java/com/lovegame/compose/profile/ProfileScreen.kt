package com.lovegame.compose.profile

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lovegame.R
import com.lovegame.ui.theme.LoveGameTheme
import com.lovegame.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    gotoLogin: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val TAG = stringResource(R.string.app_name) + "Tag " + "ProfileScreen"
    val snackbarHostState = remember { SnackbarHostState() }
    var showFullScreenImage by rememberSaveable { mutableStateOf(false) }
    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error,
                    snackbarData = it
                )
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                PhotoSelectorView(
                    onImageClick = { uri ->
                        imageUri = uri
                        showFullScreenImage = true
                    },
                    viewModel,
                    snackbarHostState
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    viewModel.signOut()
                    gotoLogin()
                }) {
                    Text(text = stringResource(R.string.sign_out))
                }
            }
            if (showFullScreenImage) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ){
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = imageUri,
                        contentDescription = null
                    )
                    BackHandler(true) {
                        showFullScreenImage = false
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoveGameTheme {
        ProfileScreen(gotoLogin = {})
    }
}