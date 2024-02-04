package com.lovegame.compose.login

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
import com.lovegame.R
import com.lovegame.compose.util.Progress
import com.lovegame.compose.util.Termstext
import com.lovegame.ui.theme.LoveGameTheme

@Composable
fun LoginScreen(
    onSignInWithGoogleClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLogInClick: (email: String, password: String) -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    loading: Boolean
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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
                            onClick = { /*TODO Forgot password*/ },
                        ) {
                            Text(text = AnnotatedString(stringResource(R.string.forgot_password_button)))
                        }
                        Button(
                            onClick = { onLogInClick(email, password) }) {
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
                        onClick = { onSignInWithGoogleClick() },
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
                Progress(isDisplayed = loading)
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    LoveGameTheme {
        LoginScreen(
            onSignInWithGoogleClick = {},
            onCreateAccountClick = {},
            onLogInClick = { email: String, password: String -> },
            onTermsClick = {},
            onPrivacyClick = {},
            remember { SnackbarHostState() },
            false
        )
    }
}