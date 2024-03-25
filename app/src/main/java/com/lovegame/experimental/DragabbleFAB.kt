package com.lovegame.experimental

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun DragabbleFAB(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
) {
    val displayWidth = Resources.getSystem().displayMetrics.widthPixels
    val displayHeight = Resources.getSystem().displayMetrics.heightPixels
    var offsetX by remember { mutableStateOf(200f) }
    var offsetY by remember { mutableStateOf(200f) }

    Box(
        Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(Color.Blue)
            .size(50.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    offsetX = offsetX.coerceIn(50f, displayWidth - 200f)
                    offsetY = offsetY.coerceIn(50f, displayHeight - 200f)
                }
            }
            .clickable {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "This is the message",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )
                }
            }
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

