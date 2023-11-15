package com.leoarmelin.connecta.ui.components

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoarmelin.connecta.ui.theme.ConnectaTheme
import com.leoarmelin.connecta.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme.colorScheme as mtc


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@SuppressLint("ModifierParameter")
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    debounce: Long = 1000,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var isDebouncing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.1f else 1.0f,
        label = "scale"
    )

    Button(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mtc.primaryContainer,
            contentColor = mtc.onPrimaryContainer,
            disabledContainerColor = mtc.primaryContainer.copy(alpha = 0.5f),
            disabledContentColor = mtc.onPrimaryContainer.copy(alpha = 0.5f),
        ),
        border = BorderStroke(2.dp, mtc.onPrimaryContainer),
        modifier = modifier
            .pointerInteropFilter {
                if (!isDebouncing) {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            isPressed = true
                        }

                        MotionEvent.ACTION_UP -> {
                            coroutineScope.launch {
                                isDebouncing = true
                                delay(debounce)
                                isDebouncing = false
                            }

                            onClick()
                            isPressed = false
                        }

                        MotionEvent.ACTION_CANCEL -> {
                            isPressed = false
                        }
                    }
                }

                true
            }
            .scale(scale)
    ) {
        Text(
            text = text,
            style = Typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewAppButton() {
    ConnectaTheme {
        AppButton(
            text = "JOGAR",
            onClick = {}
        )
    }
}