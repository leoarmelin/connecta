package com.leoarmelin.connecta.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme as mtc
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.leoarmelin.connecta.navigation.Routes.GameScreen
import com.leoarmelin.connecta.ui.theme.Typography

@Composable
fun StartScreen(
    navController: NavController = rememberNavController()
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Start Screen",
            style = Typography.titleMedium,
            color = mtc.onBackground,
            modifier = Modifier
                .clickable {
                    navController.navigate(GameScreen)
                }
        )
    }
}

@Preview
@Composable
fun PreviewStartScreen() {
    StartScreen()
}