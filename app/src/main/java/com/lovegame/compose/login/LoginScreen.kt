package com.lovegame.compose.login

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lovegame.R
import com.lovegame.compose.util.Progress
import com.lovegame.compose.util.SimpleDialog
import com.lovegame.compose.util.Termstext
import com.lovegame.compose.util.TextfieldDialog
import com.lovegame.compose.util.TwoButtonDialog
import com.lovegame.domain.util.Resource
import com.lovegame.ui.theme.LoveGameTheme
import com.lovegame.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    onCreateAccountClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    gotoProfile: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val TAG =  stringResource(R.string.app_name) + "Tag " + "LoginScreen"
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var openEmailVerificationSentDialog by remember { mutableStateOf(false) }
    var openEmailNotVerifiedDialog by remember { mutableStateOf(false) }
    var openResetPasswordDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val resourceUserData by viewModel.resourceUserData.collectAsStateWithLifecycle()
    val resourceIntentSender by viewModel.resourceIntentSender.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.getUser()
    }

    LaunchedEffect(key1 = resourceUserData) {
        Log.d(TAG, resourceUserData.toString())
        when (resourceUserData) {
            is Resource.Success -> {
                resourceUserData.data?.let {
                    if (it.isVerified) {
                        openEmailNotVerifiedDialog = false
                        gotoProfile()
                    } else {
                        openEmailNotVerifiedDialog = true
                    }
                }
                Log.d(TAG, "email verified: ${!openEmailNotVerifiedDialog}")
                viewModel.resetUserDataResource()
            }

            is Resource.Error -> {
                Log.d(TAG, resourceUserData.message)
                if (!resourceUserData.message.isEmpty()) {
                    snackbarHostState.showSnackbar(
                        message = resourceUserData.message
                    )
                }
            }

            else -> {}
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                viewModel.signInWithIntent(
                    result.data ?: return@rememberLauncherForActivityResult
                )
            }
        }
    )

    LaunchedEffect(key1 = resourceIntentSender is Resource.Success) {
        Log.d(TAG, resourceIntentSender.toString())
        when (resourceIntentSender) {
            is Resource.Success -> {
                resourceIntentSender.data?.let { intentSender ->
                    launcher.launch(
                        IntentSenderRequest.Builder(intentSender).build()
                    )
                    viewModel.resetIntentSenderResource()
                } ?: {
                    viewModel.signInGoogle()
                }
            }

            else -> {}
        }
    }

    LoveGameTheme(darkTheme = false) {
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
            Box(Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(R.color.login_background_from),
                                    colorResource(R.color.login_background_to)
                                )
                            )
                        )
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "",
                        modifier = Modifier.padding(bottom = 64.dp)
                    )
                    Text(
                        text = stringResource(R.string.log_in_text),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = stringResource(R.string.email)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = stringResource(R.string.password)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = { }),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 64.dp)
                    )
                    {
                        TextButton(
                            onClick = { openResetPasswordDialog = true },
                        ) {
                            Text(text = AnnotatedString(stringResource(R.string.forgot_password_button)))
                        }
                        Button(
                            onClick = {
                                viewModel.signInWithCredentials(email, password)
                            }) {
                            Text(text = stringResource(R.string.log_in_button))
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        onClick = { onCreateAccountClick() },
                    ) {
                        Text(text = stringResource(R.string.create_account_button))
                    }

                    Button(
                        onClick = {
                            viewModel.signInGoogle()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 16.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .height(24.dp)
                                .width(24.dp)
                        )
                        Text(text = stringResource(R.string.sign_in_with_google_button))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Termstext(
                        onTermsClick = { onTermsClick() },
                        onPrivacyClick = { onPrivacyClick() })
                }

                Progress(isDisplayed = resourceUserData is Resource.Loading)

                if (openEmailNotVerifiedDialog) {
                    TwoButtonDialog(
                        text = stringResource(R.string.verification_email_sent),
                        secondButtonText = stringResource(R.string.resend_button),
                        onDismissRequest = {
                            openEmailNotVerifiedDialog = false
                            viewModel.signOut()
                        }
                    ) {
                        viewModel.sendEmailVerification()
                        viewModel.signOut()
                        openEmailVerificationSentDialog = true
                        openEmailNotVerifiedDialog = false
                    }
                }

                if (openEmailVerificationSentDialog) {
                    SimpleDialog(text = stringResource(R.string.verification_email_sent)) {
                        openEmailVerificationSentDialog = false
                    }
                }

                if (openResetPasswordDialog) {
                    TextfieldDialog(
                        stringResource(R.string.please_enter_your_email),
                        stringResource(R.string.email),
                        buttonText = stringResource(R.string.reset),
                        onDismissRequest = {
                            openResetPasswordDialog = false
                        }
                    ) {
                        viewModel.resetPassword(it)
                        openResetPasswordDialog = false
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    LoveGameTheme {
        LoginScreen(
            onCreateAccountClick = {},
            onTermsClick = {},
            onPrivacyClick = {},
            gotoProfile = {}
        )
    }
}