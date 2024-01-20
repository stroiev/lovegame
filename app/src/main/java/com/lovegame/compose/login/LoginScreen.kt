package com.lovegame.compose.login

import android.util.Log
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lovegame.R
import com.lovegame.compose.util.ProgressBar
import com.lovegame.ui.theme.LoveGameTheme

@Composable
fun LoginScreen(
    onSignInWithGoogleClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    loading: Boolean
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    LoveGameTheme(darkTheme = false) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text(text = stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    label = { Text(text = stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    keyboardActions = KeyboardActions(onDone = { }),
                    visualTransformation = PasswordVisualTransformation(),
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
                        onClick = { /*TODO show the signup screen*/ }) {
                        Text(text = "Log in")
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
//                shape = RoundedCornerShape(6.dp),
                    onClick = { onCreateAccountClick() },
                ) {
                    Text(text = stringResource(R.string.create_account_button))
                }

//                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)

                Button(
                    onClick = { onSignInWithGoogleClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
//                shape = RoundedCornerShape(6.dp),
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

                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)){
                        append(stringResource(R.string.by_joining_you_agree_to_the))
                    }
                    pushStringAnnotation(tag = "policy", annotation = "https://google.com/policy")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline)) {
                        append(stringResource(R.string.privacy_policy))
                    }
                    pop()
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)){
                        append(stringResource(R.string.and))
                    }
                    pushStringAnnotation(tag = "terms", annotation = "https://google.com/terms")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )) {
                        append(stringResource(R.string.terms_of_use))
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedString,
                    style = MaterialTheme.typography.bodyMedium,
                    onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "policy", start = offset, end = offset).firstOrNull()?.let {
                        onPrivacyClick()
                        Log.d("policy URL", it.item)
                    }

                    annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset).firstOrNull()?.let {
                        onTermsClick()
                        Log.d("terms URL", it.item)
                    }
                })
            }
            ProgressBar(isDisplayed = loading)
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
            onTermsClick = {},
            onPrivacyClick = {},
            false
        )
    }
}