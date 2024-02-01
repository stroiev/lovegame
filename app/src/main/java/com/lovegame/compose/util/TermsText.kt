package com.lovegame.compose.util

import android.util.Log
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.lovegame.R

@Composable
fun Termstext(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
){
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)){
            append(stringResource(R.string.by_joining_you_agree_to_the))
        }
        pushStringAnnotation(tag = "policy", annotation = "https://google.com/policy")
        withStyle(style = SpanStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline)
        ) {
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
        )
        ) {
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