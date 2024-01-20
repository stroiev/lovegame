package com.lovegame.compose.terms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Terms(info: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val text: String = when (info) {
            "terms" -> LocalContext.current
                .assets
                .open("terms")
                .bufferedReader()
                .use {
                    it.readText()
                }

            "privacy" -> LocalContext.current
                .assets
                .open("privacy")
                .bufferedReader()
                .use {
                    it.readText()
                }

            else -> "Error"
        }

        Text(text = text)
    }
}