package com.lovegame.compose.createaccount

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import com.lovegame.domain.util.Resource
import com.lovegame.ui.theme.LoveGameTheme
import com.lovegame.viewmodels.CreateAccountViewModel

@Composable
fun CreateAccount(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    gotoLogin: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val TAG = stringResource(R.string.app_name) + "Tag " + "CreateAccount"
    var email by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var passwordValidLength by remember { mutableStateOf(true) }
    var retypePassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var openEmailVerificationDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val resourceUserData by viewModel.resourceUserData.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = resourceUserData) {
        Log.d(TAG, resourceUserData.toString())
        when (resourceUserData) {
            is Resource.Success -> {
                openEmailVerificationDialog = true
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
                        .imePadding()
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
                        text = stringResource(R.string.create_account_text),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailValid = true
                        },
                        label = { Text(text = stringResource(R.string.email)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        supportingText = {
                            if (!emailValid) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.email_format_is_invalid),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordValidLength = true
                        },
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
                        supportingText = {
                            if (!passwordValidLength) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.the_password_must_have_between_8_and_20_characters),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
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
                    )

                    OutlinedTextField(
                        value = retypePassword,
                        onValueChange = {
                            retypePassword = it
                            passwordsMatch = true
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = stringResource(R.string.retype_password)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null
                            )
                        },
                        keyboardActions = KeyboardActions(onDone = { }),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        supportingText = {
                            if (!passwordsMatch) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.the_entered_passwords_do_not_match),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
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
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        onClick = {
                            emailValid = viewModel.isValidEmail(email)
                            passwordValidLength = viewModel.isValidPasswordLenght(password)
                            passwordsMatch = viewModel.doPasswordsMatch(password, retypePassword)
                            if (emailValid && passwordValidLength && passwordsMatch) {
                                viewModel.createUserWithCredentials(email, password)
                            }
                        },
                    ) {
                        Text(text = stringResource(R.string.create_account_button))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Termstext(
                        onTermsClick = { onTermsClick() },
                        onPrivacyClick = { onPrivacyClick() })
                }

                Progress(isDisplayed = resourceUserData is Resource.Loading)

                if (openEmailVerificationDialog) {
                    SimpleDialog(text = stringResource(R.string.verification_email_sent)) {
                        openEmailVerificationDialog = false
                        viewModel.signOut()
                        gotoLogin()
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun CreateAccountPreview() {
    LoveGameTheme {
        CreateAccount(
            onTermsClick = {},
            onPrivacyClick = {},
            gotoLogin = {}
        )
    }
}