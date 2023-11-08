package com.leoarmelin.connecta.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoarmelin.connecta.ui.theme.ConnectaTheme
import com.leoarmelin.connecta.ui.theme.Typography
import androidx.compose.material3.MaterialTheme.colorScheme as mtc


@Composable
@SuppressLint("ModifierParameter")
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mtc.primaryContainer,
            contentColor = mtc.onPrimaryContainer,
            disabledContainerColor = mtc.primaryContainer.copy(alpha = 0.5f),
            disabledContentColor = mtc.onPrimaryContainer.copy(alpha = 0.5f),
        ),
        modifier = modifier
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